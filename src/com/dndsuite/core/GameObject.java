package com.dndsuite.core;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.dndsuite.core.event_groups.EventGroup;
import com.dndsuite.core.json.JSONLoader;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.core.json.parsers.subevents.uninvokable.AbilityScoreCalculation;
import com.dndsuite.core.json.parsers.subevents.uninvokable.DamageCalculation;
import com.dndsuite.core.json.parsers.subevents.uninvokable.UnequipItem;
import com.dndsuite.exceptions.CannotUnequipItemException;
import com.dndsuite.exceptions.JSONFormatException;
import com.dndsuite.exceptions.SubeventMismatchException;
import com.dndsuite.exceptions.UUIDDoesNotExistException;
import com.dndsuite.exceptions.UUIDNotAssignedException;
import com.dndsuite.maths.Vector;
import com.dndsuite.maths.dice.DamageDiceGroup;

/**
 * GameObject is a class which represents any game piece which would typically
 * appear on the game board. A GameObject may or may not have the capacity to
 * invoke Tasks/Events (buildings cannot typically take actions, for example,
 * despite being GameObjects), and a GameObject may or may not include an
 * inventory. But all GameObjects have hit points and position/rotation vectors.
 * 
 * @author Calvin Withun
 *
 */
public class GameObject extends JSONLoader implements UUIDTableElement, Subject {

	ArrayList<EventGroup> queuedEvents;
	// TODO: will observer relations ever need to be saved as json data?
	ArrayList<Observer> observers;

	/**
	 * GameObject constructor for loading from save JSON files.
	 * 
	 * @param json - the JSON data stored in a save file
	 */
	public GameObject(JSONObject json) {
		super(json);
		queuedEvents = new ArrayList<EventGroup>();
		observers = new ArrayList<Observer>();

		UUIDTable.addToTable(this);
		VirtualBoard.add(this);
	}

	/**
	 * GameObject constructor for loading a GameObject from template JSON files.
	 * 
	 * @param file - the path to a file, as a continuation of the file path
	 *             "resources/json/gameobjects/..."
	 * @param pos  - an array indicating the position to place the new GameObject in
	 *             the form of [x,y,z]
	 * @param rot  - an array indicating the rotation of the new GameObject about
	 *             each dimensional axis in the form of [x,y,z]
	 */
	public GameObject(String file, Vector pos, Vector rot) {
		super("gameobjects/" + file);
		queuedEvents = new ArrayList<EventGroup>();
		observers = new ArrayList<Observer>();
		setPos(pos);
		setRot(rot);

		UUIDTable.addToTable(this);
		VirtualBoard.add(this);
	}

	@Override
	public long getUUID() throws UUIDNotAssignedException {
		if (json.containsKey("uuid")) {
			return (long) json.get("uuid");
		}
		throw new UUIDNotAssignedException(this);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void assignUUID(long uuid) {
		json.put("uuid", uuid);
	}

	public Vector getPos() {
		JSONArray pos = (JSONArray) json.get("pos");
		return new Vector((double) pos.get(0), (double) pos.get(1), (double) pos.get(2));
	}

	public Vector getRot() {
		JSONArray rot = (JSONArray) json.get("rot");
		return new Vector((double) rot.get(0), (double) rot.get(1), (double) rot.get(2));
	}

	@SuppressWarnings("unchecked")
	public void setPos(Vector newPos) {
		JSONArray pos = (JSONArray) json.getOrDefault("pos", new JSONArray());
		pos.clear();
		pos.add(newPos.x());
		pos.add(newPos.y());
		pos.add(newPos.z());

		updateObservers();
	}

	@SuppressWarnings("unchecked")
	public void setRot(Vector newRot) {
		JSONArray rot = (JSONArray) json.getOrDefault("rot", new JSONArray());
		rot.clear();
		rot.add(newRot.x());
		rot.add(newRot.y());
		rot.add(newRot.z());

		updateObservers();
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void parseTemplate() {
		// iterate across all known Effects
		JSONArray effectNames = (JSONArray) json.remove("effects");
		JSONArray effectUUIDs = new JSONArray();
		while (effectNames.size() > 0) {
			// TODO: ensure source/target UUIDs are provided here
			String effectName = (String) effectNames.remove(0);
			Effect e = new Effect(effectName, this, this);
			try {
				effectUUIDs.add(e.getUUID());
			} catch (UUIDNotAssignedException ex) {
				ex.printStackTrace();
			}

		}
		json.put("effects", effectUUIDs);

		// iterate across all known Tasks
		JSONArray taskNames = (JSONArray) json.remove("tasks");
		JSONArray taskUUIDs = new JSONArray();
		while (taskNames.size() > 0) {
			String taskName = (String) taskNames.remove(0);
			Task t = new Task(taskName);
			try {
				taskUUIDs.add(t.getUUID());
			} catch (UUIDNotAssignedException ex) {
				ex.printStackTrace();
			}
		}
		json.put("tasks", taskUUIDs);

		// iterate across all held items
		if (json.containsKey("inventory")) {
			// iterate across all non-equipped items and add to inventory
			JSONObject inventory = (JSONObject) json.remove("inventory");
			JSONArray items = (JSONArray) inventory.remove("items");
			JSONArray itemUUIDs = new JSONArray();
			while (items.size() > 0) {
				String itemName = (String) items.remove(0);
				Item i = new Item(itemName);
				try {
					itemUUIDs.add(i.getUUID());
				} catch (UUIDNotAssignedException ex) {
					ex.printStackTrace();
				}
			}

			JSONObject newInventory = new JSONObject();
			newInventory.put("items", itemUUIDs);

			// iterate across all equipped items and add to inventory
			for (Object o : inventory.keySet()) {
				if (o instanceof JSONArray) {
					continue;
				}
				String key = (String) o;
				String itemName = (String) inventory.get(key);
				Item i = new Item(itemName);
				try {
					newInventory.put(key, i.getUUID());
					itemUUIDs.add(i.getUUID());
				} catch (UUIDNotAssignedException ex) {
					ex.printStackTrace();
				}
			}
			json.put("inventory", newInventory);
		}

	}

	@Override
	public void addObserver(Observer o) {
		observers.add(o);

	}

	@Override
	public void removeObserver(Observer o) {
		observers.remove(o);
	}

	@Override
	public void updateObservers() {
		for (Observer o : observers) {
			o.update(this);
		}
	}

	/**
	 * This function allows a GameObject to pass a Subevent to its Effects in order
	 * to be assessed and possibly modified
	 * 
	 * @param s - the Subevent to be assessed
	 * @return true if s was changed, false if s was not changed
	 */
	public boolean processSubevent(Subevent s) {
		JSONArray effects = (JSONArray) json.get("effects");
		boolean changed = false;
		for (int i = 0; i < effects.size(); i++) {
			long uuid = (long) effects.get(i);
			try {
				Effect effect = (Effect) UUIDTable.get(uuid);
				changed |= effect.processSubevent(s);
			} catch (UUIDDoesNotExistException ex) {
				ex.printStackTrace();
			}
		}
		return changed;
	}

	/**
	 * This function allows a GameObject to invoke a Task in order to queue
	 * EventGroups which may be invoked at a later time. This converts resources
	 * into actual behavior.
	 * 
	 * @NOTE resources are not yet implemented in this library, so all Tasks are
	 *       without cost at this time
	 * 
	 * @param uuid - the UUID of the Task to be invoked
	 */
	public void invokeTask(long uuid) {
		// TODO: dedicate a subevent to collecting all Tasks available to this
		try {
			UUIDTableElement element = UUIDTable.get(uuid);
			if (element instanceof Task) {
				Task t = (Task) element;
				t.invoke(this);
			}
		} catch (UUIDDoesNotExistException ex) {
			ex.printStackTrace();
		} catch (JSONFormatException ex) {
			ex.printStackTrace();
		}

		updateObservers();
	}

	/**
	 * This function allows a GameObject to invoke a particular Event which has been
	 * queued as a part of an EventGroup. This entails no resource cost, and
	 * consumes the entire EventGroup which the desired Event is contained within.
	 * After calling this function, the client will need to dequeue the Event from
	 * the ReceptorQueue class and provide it with targeting information through the
	 * resume() function (startPos, pointTo Vectors).
	 * 
	 * @param e - the Event to be invoked
	 */
	public void invokeQueuedEvent(Event e) {
		EventGroup container = null;
		for (EventGroup group : queuedEvents) {
			if (group.contains(e)) {
				container = group;
				e.pause();
				break;
			}
		}

		if (container != null) {
			queuedEvents.remove(container);
		}

		updateObservers();
	}

	/**
	 * This function allows a GameObject to queue an EventGroup for later reference.
	 * 
	 * @param group - the EventGroup to be queued
	 */
	public void queueEventGroup(EventGroup group) {
		queuedEvents.add(group);
		updateObservers();
	}

	public ArrayList<EventGroup> getQueuedEventGroups() {
		return queuedEvents;
	}

	public void clearQueuedEvents() {
		queuedEvents.clear();
		updateObservers();
	}

	/**
	 * This function allows a GameObject to recognize itself as the target of a new
	 * Effect. If an Effect with the same name is already applied, nothing happens.
	 * 
	 * @param effect - the Effect to be applied to the GameObject
	 */
	@SuppressWarnings("unchecked")
	public void addEffect(Effect effect) {
		JSONArray effectUUIDs = (JSONArray) json.get("effects");
		boolean redundant = false;
		for (Object o : effectUUIDs) {
			long uuid = (long) o;
			try {
				Effect e = (Effect) UUIDTable.get(uuid);
				if (e.toString().equals(effect.toString())) {
					redundant = true;
				}
			} catch (UUIDDoesNotExistException ex) {
				ex.printStackTrace();
			}
		}
		if (!redundant) {
			try {
				effectUUIDs.add(effect.getUUID());
				updateObservers();
			} catch (UUIDNotAssignedException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void removeEffect(Effect effect) {
		JSONArray effectUUIDs = (JSONArray) json.get("effects");
		try {
			effectUUIDs.remove(effect.getUUID());
			updateObservers();
		} catch (UUIDNotAssignedException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * This function returns the functional modifier for an ability of a GameObject.
	 * 
	 * @param ability - the ability score whose modifier is desired
	 * @return the modifier for the ability score in question
	 */
	@SuppressWarnings("unchecked")
	public long getAbilityModifier(String ability) {
		JSONObject ascJson = new JSONObject();
		ascJson.put("subevent", "ability_score_calculation");
		ascJson.put("ability", ability);
		AbilityScoreCalculation asc = new AbilityScoreCalculation();
		try {
			asc.parse(ascJson, null, this, this);
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
		}

		long abilityScoreBuffer = asc.get() - 10;
		if (abilityScoreBuffer < 0) {
			abilityScoreBuffer--;
		}
		return abilityScoreBuffer / 2;
	}

	/**
	 * This function accepts a DamageCalculation Subevent and passes the damage
	 * value within it to the other takeDamage() function. This is the final step in
	 * the standard damage-dealing process via Subevents.
	 * 
	 * @param damage - the Subevent containing the final damage value of a damage
	 *               Subevent
	 */
	public void takeDamage(DamageCalculation damage) {
		long tmp = 0;
		for (DamageDiceGroup group : damage.getDamageDice()) {
			tmp += group.getDamageValue();
		}
		takeDamage(tmp);
	}

	/**
	 * This function decrements the hit points of the GameObject, beginning with the
	 * temporary hit points before moving on to the actual hit points. If the
	 * GameObject is reduced below 0 hit points, it is set to 0 hit points.
	 * 
	 * @param damage - the final type-independent damage total of a damage Subevent
	 */
	@SuppressWarnings("unchecked")
	public void takeDamage(long damage) {
		JSONObject health = (JSONObject) json.get("health");
		long base = (long) health.get("base");
		long max = (long) health.get("max");
		long tmp = (long) health.get("tmp");
		long current = (long) health.get("current");

		if (tmp > 0) {
			tmp -= damage;
			if (tmp < 0) {
				damage = -tmp;
				tmp = 0;
			} else {
				damage = 0;
			}
			health.put("tmp", tmp);
		}

		current -= damage;
		if (current < 0) {
			current = 0;
			// TODO: death or death saves here
			// instant-kill check would happen here based on magnitude of current
		}
		health.put("current", current);

		updateObservers();
	}

	public JSONObject getHealth() {
		return (JSONObject) json.get("health");
	}

	/**
	 * This function returns the proficiency bonus of a GameObject as a function of
	 * its level.
	 * 
	 * @return the GameObject's proficiency bonus, or 0 if the GameObject does not
	 *         have level data
	 */
	public long getProficiencyBonus() {
		if (json.containsKey("level")) {
			long level = (long) json.get("level");
			return 2L + (level - 1L) / 4L;
		}
		return 0L;
	}

	/**
	 * This function causes the GameObject to equip an Item in its mainhand slot, if
	 * possible. If the GameObject does not yet possess this Item, it is added to
	 * the GameObject's inventory as well. A two-handed Item is equipped in both
	 * hands through this function, and versatile Items are by default only held by
	 * one hand.
	 * 
	 * @param uuid - the UUID of the Item to be equipped
	 * @throws CannotUnequipItemException if the GameObject needs to stow its
	 *                                    mainhand Item to equip the new Item, but
	 *                                    it cannot, this exception is thrown. The
	 *                                    Item is added to the GameObject's
	 *                                    inventory, ragardless.
	 */
	@SuppressWarnings("unchecked")
	public void equipMainhand(long uuid) throws CannotUnequipItemException {
		JSONObject inventory = (JSONObject) json.get("inventory");
		JSONArray items = (JSONArray) inventory.get("items");
		if (!items.contains(uuid)) {
			items.add(uuid);
		}

		UnequipItem s = new UnequipItem();
		JSONObject sJson = new JSONObject();
		sJson.put("subevent", "unequip_item");

		try {
			Item item = (Item) UUIDTable.get(uuid);
			if ((long) inventory.get("mainhand") != -1L) {
				Item mainhand = (Item) inventory.get("mainhand");
				sJson.put("item_uuid", mainhand.getUUID());
				s.parse(sJson, null, this, this);

				if (s.getSuccess()) {
					mainhand.unequipBy(this);
					inventory.put("mainhand", -1L);
				} else {
					throw new CannotUnequipItemException(mainhand);
				}
			}
			if (item.hasTag("two_handed")) {
				if ((long) inventory.get("offhand") != -1L) {
					Item offhand = (Item) inventory.get("offhand");
					sJson.put("item_uuid", offhand.getUUID());
					s.parse(sJson, null, this, this);

					if (s.getSuccess()) {
						offhand.unequipBy(this);
						inventory.put("offhand", uuid);
					} else {
						throw new CannotUnequipItemException(offhand);
					}
				} else {
					inventory.put("offhand", uuid);
				}
			}
			item.equipBy(this);
			inventory.put("mainhand", uuid);

		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
		} catch (UUIDDoesNotExistException ex) {
			ex.printStackTrace();
		} catch (UUIDNotAssignedException ex) {
			ex.printStackTrace();
		}

		updateObservers();
	}

	/**
	 * This function causes the GameObject to equip an Item in its offhand slot, if
	 * possible. If the GameObject does not yet possess this Item, it is added to
	 * the GameObject's inventory as well. A two-handed Item is equipped in both
	 * hands through this function, and versatile Items are by default only held by
	 * one hand. A GameObject cannot switch to holding a versatile Item with two
	 * hands if the Item is being held in the offhand slot.
	 * 
	 * @param uuid - the UUID of the Item to be equipped
	 * @throws CannotUnequipItemException if the GameObject needs to stow its
	 *                                    offhand Item to equip the new Item, but it
	 *                                    cannot, this exception is thrown. The Item
	 *                                    is added to the GameObject's inventory,
	 *                                    ragardless.
	 */
	@SuppressWarnings("unchecked")
	public void equipOffhand(long uuid) throws CannotUnequipItemException {
		JSONObject inventory = (JSONObject) json.get("inventory");
		JSONArray items = (JSONArray) inventory.get("items");
		if (!items.contains(uuid)) {
			items.add(uuid);
		}

		UnequipItem s = new UnequipItem();
		JSONObject sJson = new JSONObject();
		sJson.put("subevent", "unequip_item");

		try {
			Item item = (Item) UUIDTable.get(uuid);
			if ((long) inventory.get("offhand") != -1L) {
				Item offhand = (Item) inventory.get("offhand");
				sJson.put("item_uuid", offhand.getUUID());
				s.parse(sJson, null, this, this);

				if (s.getSuccess()) {
					offhand.unequipBy(this);
					inventory.put("offhand", -1L);
				} else {
					throw new CannotUnequipItemException(offhand);
				}
			}
			if (item.hasTag("two_handed")) {
				if ((long) inventory.get("mainhand") != -1L) {
					Item mainhand = (Item) inventory.get("mainhand");
					sJson.put("item_uuid", mainhand.getUUID());
					s.parse(sJson, null, this, this);

					if (s.getSuccess()) {
						mainhand.unequipBy(this);
						inventory.put("mainhand", uuid);
					} else {
						throw new CannotUnequipItemException(mainhand);
					}
				} else {
					inventory.put("mainhand", uuid);
				}
			}
			item.equipBy(this);
			inventory.put("offhand", uuid);

		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
		} catch (UUIDDoesNotExistException ex) {
			ex.printStackTrace();
		} catch (UUIDNotAssignedException ex) {
			ex.printStackTrace();
		}

		updateObservers();
	}

	/**
	 * This function causes the GameObject to stow an Item in its mainhand slot, if
	 * possible. A two-handed Item is stowed from both hands through this function,
	 * as well as a versatile item which is being held in both hands.
	 * 
	 * @throws CannotUnequipItemException if the GameObject cannot stow its mainhand
	 *                                    Item, this exception is thrown.
	 */
	@SuppressWarnings("unchecked")
	public void stowMainhand() throws CannotUnequipItemException {
		JSONObject inventory = (JSONObject) json.get("inventory");
		long uuid = (long) inventory.get("mainhand");

		UnequipItem s = new UnequipItem();
		JSONObject sJson = new JSONObject();
		sJson.put("subevent", "unequip_item");

		try {
			Item mainhand = (Item) UUIDTable.get(uuid);
			sJson.put("item_uuid", mainhand.getUUID());
			s.parse(sJson, null, this, this);

			if (s.getSuccess()) {
				if (uuid == (long) inventory.get("offhand")) {
					inventory.put("offhand", -1L);
				}
				mainhand.unequipBy(this);
				inventory.put("mainhand", -1L);
			} else {
				throw new CannotUnequipItemException(mainhand);
			}
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
		} catch (UUIDNotAssignedException ex) {
			ex.printStackTrace();
		} catch (UUIDDoesNotExistException ex) {
			ex.printStackTrace();
		}

		updateObservers();
	}

	/**
	 * This function causes the GameObject to stow an Item in its offhand slot, if
	 * possible. A two-handed Item is stowed from both hands through this function,
	 * as well as a versatile item which is being held in both hands.
	 * 
	 * @throws CannotUnequipItemException if the GameObject cannot stow its offhand
	 *                                    Item, this exception is thrown.
	 */
	@SuppressWarnings("unchecked")
	public void stowOffhand() throws CannotUnequipItemException {
		JSONObject inventory = (JSONObject) json.get("inventory");
		long uuid = (long) inventory.get("offhand");

		UnequipItem s = new UnequipItem();
		JSONObject sJson = new JSONObject();
		sJson.put("subevent", "unequip_item");

		try {
			Item mainhand = (Item) UUIDTable.get(uuid);
			sJson.put("item_uuid", mainhand.getUUID());
			s.parse(sJson, null, this, this);

			if (s.getSuccess()) {
				if (uuid == (long) inventory.get("offhand")) {
					inventory.put("offhand", -1L);
				}
				mainhand.unequipBy(this);
				inventory.put("mainhand", -1L);
			} else {
				throw new CannotUnequipItemException(mainhand);
			}
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
		} catch (UUIDNotAssignedException ex) {
			ex.printStackTrace();
		} catch (UUIDDoesNotExistException ex) {
			ex.printStackTrace();
		}

		updateObservers();
	}

	/**
	 * This function allows a GameObject to change between holding a versatile Item
	 * with one hand, or with two hands. Switching from one hand to two hands only
	 * works if the Item is held in the mainhand slot.
	 * 
	 * @throws CannotUnequipItemException if the GameObject cannot stow its offhand
	 *                                    Item and needs to, this exception is
	 *                                    thrown.
	 */
	@SuppressWarnings("unchecked")
	public void toggleVersatile() throws CannotUnequipItemException {
		JSONObject inventory = (JSONObject) json.get("inventory");
		long mainhandUUID = (long) inventory.get("mainhand");
		long offhandUUID = (long) inventory.get("offhand");
		if (mainhandUUID == -1L) {
			return;
		}

		UnequipItem s = new UnequipItem();
		JSONObject sJson = new JSONObject();
		sJson.put("subevent", "unequip_item");

		try {
			Item mainhand = (Item) UUIDTable.get(mainhandUUID);
			if (mainhand.hasTag("versatile")) {
				if (mainhandUUID != offhandUUID) {
					if (offhandUUID != -1L) {
						Item offhand = (Item) UUIDTable.get(offhandUUID);
						sJson.put("item_uuid", offhandUUID);
						s.parse(sJson, null, this, this);
						if (s.getSuccess()) {
							offhand.unequipBy(this);
							inventory.put("offhand", -1L);
						} else {
							throw new CannotUnequipItemException(offhand);
						}
					}
					inventory.put("offhand", mainhandUUID);
				} else {
					inventory.put("offhand", -1L);
				}
			}
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
		} catch (UUIDDoesNotExistException ex) {
			ex.printStackTrace();
		}

		updateObservers();
	}

	public Item getMainhand() {
		JSONObject inventory = (JSONObject) json.get("inventory");
		if ((long) inventory.get("mainhand") == -1L) {
			return null;
		}

		long uuid = (long) inventory.get("mainhand");
		try {
			return (Item) UUIDTable.get(uuid);
		} catch (UUIDDoesNotExistException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public Item getOffhand() {
		JSONObject inventory = (JSONObject) json.get("inventory");
		if ((long) inventory.get("offhand") == -1L) {
			return null;
		}

		long uuid = (long) inventory.get("offhand");
		try {
			return (Item) UUIDTable.get(uuid);
		} catch (UUIDDoesNotExistException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public boolean hasItem(long uuid) {
		JSONObject inventory = (JSONObject) json.get("inventory");
		JSONArray items = (JSONArray) inventory.get("items");
		return items.contains(uuid);
	}

}
