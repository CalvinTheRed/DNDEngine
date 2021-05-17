package com.dndsuite.core;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.dndsuite.core.json.JSONLoader;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.core.json.parsers.subevents.AbilityScoreCalculation;
import com.dndsuite.core.json.parsers.subevents.DamageCalculation;
import com.dndsuite.core.json.parsers.subevents.UnequipItem;
import com.dndsuite.exceptions.CannotUnequipItemException;
import com.dndsuite.exceptions.JSONFormatException;
import com.dndsuite.exceptions.SubeventMismatchException;
import com.dndsuite.exceptions.UUIDDoesNotExistException;
import com.dndsuite.exceptions.UUIDNotAssignedException;
import com.dndsuite.maths.Vector;
import com.dndsuite.maths.dice.DamageDiceGroup;

public class GameObject extends JSONLoader implements UUIDTableElement {

	ArrayList<EventGroup> queuedEvents;

	public GameObject(JSONObject json) {
		super(json);
		queuedEvents = new ArrayList<EventGroup>();

		UUIDTable.addToTable(this);
		VirtualBoard.addGameObject(this);
	}

	@SuppressWarnings("unchecked")
	public GameObject(String file, Vector pos, Vector rot) {
		super("gameobjects/" + file);
		queuedEvents = new ArrayList<EventGroup>();

		JSONArray position = new JSONArray();
		position.add(pos.x());
		position.add(pos.y());
		position.add(pos.z());
		json.put("pos", position);

		JSONArray rotation = new JSONArray();
		rotation.add(rot.x());
		rotation.add(rot.y());
		rotation.add(rot.z());
		json.put("rot", rotation);

		UUIDTable.addToTable(this);
		VirtualBoard.addGameObject(this);
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

	@Override
	@SuppressWarnings("unchecked")
	protected void parseResourceData() {
		JSONArray effectNames = (JSONArray) json.remove("effects");
		JSONArray effectUUIDs = new JSONArray();
		while (effectNames.size() > 0) {
			// TODO: ensure source/target UUIDs are provided here
			String effectName = (String) effectNames.remove(0);
			Effect e = new Effect(effectName, this, this);
			UUIDTable.addToTable(e);
			try {
				effectUUIDs.add(e.getUUID());
			} catch (UUIDNotAssignedException ex) {
				ex.printStackTrace();
			}

		}
		json.put("effects", effectUUIDs);

		JSONArray taskNames = (JSONArray) json.remove("tasks");
		JSONArray taskUUIDs = new JSONArray();
		while (taskNames.size() > 0) {
			String taskName = (String) taskNames.remove(0);
			Task t = new Task(taskName);
			UUIDTable.addToTable(t);
			try {
				taskUUIDs.add(t.getUUID());
			} catch (UUIDNotAssignedException ex) {
				ex.printStackTrace();
			}
		}
		json.put("tasks", taskUUIDs);
	}

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

	public void invokeTask(long uuid) {
		// TODO: dedicate a subevent to collecting all Tasks available to this
		try {
			UUIDTableElement element = UUIDTable.get(uuid);
			if (element instanceof Task) {
				Task t = (Task) element;
				t.invoke(this);
			}
		} catch (UUIDDoesNotExistException e) {
			e.printStackTrace();
			return;
		}
	}

	public void invokeQueuedEvent(Event e, Vector start, Vector end) {
		EventGroup container = null;
		for (EventGroup group : queuedEvents) {
			if (group.contains(e)) {
				container = group;
				e.invoke(start, end, this);
				break;
			}
		}

		if (container != null) {
			queuedEvents.remove(container);
		}
	}

	public void queueEventGroup(EventGroup group) {
		queuedEvents.add(group);
	}

	public void clearQueuedEvents() {
		queuedEvents.clear();
	}

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

	public void takeDamage(DamageCalculation damage) {
		for (DamageDiceGroup group : damage.getDamageDice()) {
			takeDamage(group.getDamageValue());
		}
	}

	@SuppressWarnings("unchecked")
	private void takeDamage(long damage) {
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
	}

	public JSONObject getHealth() {
		return (JSONObject) json.get("health");
	}

	public long getProficiencyBonus() {
		if (json.containsKey("level")) {
			long level = (long) json.get("level");
			return 2L + (level - 1L) / 4L;
		}
		return 0L;
	}

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
		} catch (JSONFormatException ex) {
			ex.printStackTrace();
		} catch (UUIDDoesNotExistException ex) {
			ex.printStackTrace();
		} catch (UUIDNotAssignedException ex) {
			ex.printStackTrace();
		}
	}

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
		} catch (JSONFormatException ex) {
			ex.printStackTrace();
		} catch (UUIDDoesNotExistException ex) {
			ex.printStackTrace();
		} catch (UUIDNotAssignedException ex) {
			ex.printStackTrace();
		}
	}

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
		} catch (JSONFormatException ex) {
			ex.printStackTrace();
		} catch (UUIDDoesNotExistException ex) {
			ex.printStackTrace();
		}
	}

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
		} catch (JSONFormatException ex) {
			ex.printStackTrace();
		} catch (UUIDDoesNotExistException ex) {
			ex.printStackTrace();
		}
	}

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
		} catch (JSONFormatException ex) {
			ex.printStackTrace();
		} catch (UUIDDoesNotExistException ex) {
			ex.printStackTrace();
		}
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
