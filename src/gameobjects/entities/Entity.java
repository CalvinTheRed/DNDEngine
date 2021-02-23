package gameobjects.entities;

import java.util.LinkedList;

import dnd.combat.DamageDiceGroup;
import dnd.data.Alignment;
import dnd.data.CreatureType;
import dnd.data.DamageType;
import dnd.data.Language;
import dnd.data.SizeCategory;
import dnd.data.WeaponType;
import dnd.effects.Effect;
import dnd.events.Damage;
import dnd.events.Event;
import dnd.events.eventgroups.EventGroup;
import dnd.items.Inventory;
import dnd.tasks.Task;
import gameobjects.GameObject;
import maths.Vector;
import maths.dice.DiceGroup;

/**
 * An abstract class which represents a game piece which has the ability to
 * perform actions
 * 
 * @author calvi
 *
 */
public abstract class Entity extends GameObject {

	/*
	 * Class constants
	 */

	public static final int STR = 0;
	public static final int DEX = 1;
	public static final int CON = 2;
	public static final int INT = 3;
	public static final int WIS = 4;
	public static final int CHA = 5;

	public static final int ACROBATICS = 0;
	public static final int ANIMAL_HANDLING = 1;
	public static final int ARCANA = 2;
	public static final int ATHLETICS = 3;
	public static final int DECEPTION = 4;
	public static final int HISTORY = 5;
	public static final int INSIGHT = 6;
	public static final int INTIMIDATION = 7;
	public static final int INVESTIGATION = 8;
	public static final int MEDICINE = 9;
	public static final int NATURE = 10;
	public static final int PERCEPTION = 11;
	public static final int PERFORMANCE = 12;
	public static final int PERSUASION = 13;
	public static final int RELIGION = 14;
	public static final int SLEIGHT_OF_HAND = 15;
	public static final int STEALTH = 16;
	public static final int SURVIVAL = 17;

	// ---- Structural variables ----

	protected LinkedList<Effect> activeEffects;
	protected LinkedList<Entity> targets; // TODO is this used?
	protected LinkedList<EventGroup> eventQueue;
	protected LinkedList<Task> availableTasks;
	protected LinkedList<Task> baseTasks;

	// ---- Entity data ----

	protected int baseArmorClass;
	protected int experience;
	protected int health;
	protected int healthBase;
	protected int healthMax;
	protected int healthTmp;
	protected int level;
	protected int[] abilityScores;
	protected int[] baseAbilityScores;
	protected boolean[] saveProficiency;
	protected boolean[] skillExpertise;
	protected boolean[] skillProficiency;
	protected Alignment alignment;
	protected CreatureType type;
	protected SizeCategory size;
	protected Effect concentration; // TODO: concentrating effects need to be able to support multiple targets at a
									// time
	protected Inventory inventory;
	protected LinkedList<DamageType> immunities;
	protected LinkedList<DamageType> resistances;
	protected LinkedList<DamageType> vulnerabilities;
	protected LinkedList<Language> languages;
	protected LinkedList<WeaponType> weaponProficiency;

	// ---- Constructors ----

	/**
	 * Constructor for class Entity
	 * 
	 * @param name ({@code String}) the name of the Entity
	 * @param pos  ({@code Vector}) the coordinates at which the Entity is located
	 * @param rot  ({@code Vector}) a vector in parallel with the direction the
	 *             Entity is facing
	 */
	public Entity(String name, Vector pos, Vector rot) {
		super(name, pos, rot);
		this.name = name;

		abilityScores = new int[6];
		baseAbilityScores = new int[6];
		saveProficiency = new boolean[18];
		skillProficiency = new boolean[18];
		skillExpertise = new boolean[18];
		inventory = new Inventory();
		immunities = new LinkedList<DamageType>();
		resistances = new LinkedList<DamageType>(); // TODO: turn into Effect objects rather than attributes of Entity
		vulnerabilities = new LinkedList<DamageType>();
		activeEffects = new LinkedList<Effect>();
		targets = new LinkedList<Entity>();
		eventQueue = new LinkedList<EventGroup>();
		languages = new LinkedList<Language>();
		availableTasks = new LinkedList<Task>();
		baseTasks = new LinkedList<Task>();
		weaponProficiency = new LinkedList<WeaponType>();
	}

	// ---- Static class methods ----

	/**
	 * This static function returns a String equivalent for an ability score index
	 * in the range [0,5]
	 * 
	 * @param abilityIndex ({@code int}) index of an ability score
	 * @return {@code String} the String equivalent of the passed ability score
	 *         index
	 */
	public static String getAbility(int abilityIndex) {
		if (abilityIndex == STR) {
			return "STR";
		} else if (abilityIndex == DEX) {
			return "DEX";
		} else if (abilityIndex == CON) {
			return "CON";
		} else if (abilityIndex == INT) {
			return "INT";
		} else if (abilityIndex == WIS) {
			return "WIS";
		} else if (abilityIndex == CHA) {
			return "CHA";
		}
		return "";
	}

	// ---- Methods concerning targets ----

	/**
	 * This function is to be removed
	 * 
	 * @deprecated
	 * @param e
	 * @return
	 */
	public boolean addTarget(Entity e) {
		if (targets.contains(e)) {
			return false;
		}
		targets.add(e);
		return true;
	}

	/**
	 * This function is to be removed
	 * 
	 * @deprecated
	 * @param list
	 */
	public void addTargets(LinkedList<Entity> list) {
		for (Entity e : list) {
			if (!targets.contains(e)) {
				targets.add(e);
			}
		}
	}

	/**
	 * This function is to be removed
	 * 
	 * @deprecated
	 * @param e
	 * @return
	 */
	public boolean removeTarget(Entity e) {
		return targets.remove(e);
	}

	/**
	 * This function is to be removed
	 * 
	 * @deprecated
	 */
	public void clearTargets() {
		targets.clear();
	}

	// ---- Methods concerning tasks ----

	/**
	 * This function adds a new Task to the Entity's base set of available Tasks
	 * 
	 * @param task ({@code Task}) the Task to be added
	 * @return {@code boolean} whether the Task was successfully added
	 */
	public boolean addBaseTask(Task task) {
		if (!baseTasks.contains(task)) {
			baseTasks.add(task);
			return true;
		}
		return false;
	}

	/**
	 * This function adds a Task to the Entity's current set of available Tasks (but
	 * not the Entity's base set of Tasks)
	 * 
	 * @param task ({@code Task}) the Task to be added
	 * @return {@code boolean} whether the Task was successfully added
	 */
	public boolean addTask(Task task) {
		if (!availableTasks.contains(task)) {
			availableTasks.add(task);
			return true;
		}
		return false;
	}

	/**
	 * This function returns a list of the Entity's current available Tasks
	 * 
	 * @return {@code LinkedList<Task>} available Tasks
	 */
	public LinkedList<Task> getTasks() {
		return availableTasks;
	}

	/**
	 * This function clears the Entity's available Task list, then re-populates it
	 * with Tasks from its base Task list
	 */
	public void resetTasks() {
		availableTasks.clear();
		availableTasks.addAll(baseTasks);
	}

	/**
	 * This function allows the Entity to expend action economy to invoke a Task and
	 * gain access to its contained EventGroups
	 * 
	 * @param index ({@code int}) the index of the Task to be invoked
	 * @return {@code boolean} whether the Task was successfully invoked
	 */
	public boolean invokeTask(int index) {
		if (availableTasks.size() > index) {
			return availableTasks.get(index).invoke(this);
		}
		return false;
	}

	// ---- Methods concerning the event queue ----

	/**
	 * This function adds an EventGroup object to its event queue, effectively
	 * granting it the ability to enact an Event from the passed EventGroup
	 * 
	 * @param group ({@code EventGroup}) the EventGroup to be queued
	 * @return
	 */
	public boolean queueEventGroup(EventGroup group) {
		if (!eventQueue.contains(group)) {
			eventQueue.add(group);
			return true;
		}
		return false;
	}

	/**
	 * This function returns the list of EventGroup objects which have been queued
	 * by this Entity
	 * 
	 * @return {@code LinkedList<EventGroup>} event queue
	 */
	public LinkedList<EventGroup> getEventQueue() {
		return eventQueue;
	}

	/**
	 * This function clears the Entity's EventGroup queue, effectively removing its
	 * ability to enact Events until a new EventGroup is queued
	 */
	public void clearEventQueue() {
		eventQueue.clear();
	}

	/**
	 * This function allows an Entity to enact a particular Event from a queued
	 * EventGroup object. After invoking that Event, its corresponding EventGroup
	 * will be dequeued
	 * 
	 * @param groupIndex ({@code int}) the index of the EventGroup which contains
	 *                   the desired Event
	 * @param eventIndex ({@code int}) the index of the desired Event within its
	 *                   EventGroup
	 * @param targetPos  ({@code Vector}) the coordinate at which the desired Event
	 *                   is being invoked
	 * @return {@code boolean} whether the Event was successfully invoked
	 */
	public boolean invokeQueuedEvent(int groupIndex, int eventIndex, Vector targetPos) {
		if (eventQueue.size() > groupIndex && eventQueue.get(groupIndex).getEvents().size() > eventIndex) {
			eventQueue.get(groupIndex).getEvents().get(eventIndex).invoke(this, targetPos);
			eventQueue.remove(groupIndex);
			return true;
		}
		return false;
	}

	// ---- Methods concerning events ----

	/**
	 * This function adds an Effect to the Entity
	 * 
	 * @param e ({@code Effect}) the Effect to be added
	 * @return {@code boolean} whether the Effect was successfully applied
	 */
	public boolean addEffect(Effect e) {
		if (activeEffects.contains(e)) {
			return false;
		}

		if (e.getSequencingPriority() == Effect.HI_PRIORITY) {
			activeEffects.addFirst(e);
		} else {
			activeEffects.addLast(e);
		}
		System.out.println(this + " now observing " + e);
		return true;
	}

	/**
	 * This function allows an Entity's Effects to modify an Event (either incoming
	 * or outgoing) as appropriate
	 * 
	 * @param e      ({@code Event}) the Event being processed
	 * @param source ({@code Entity}) the Entity which created the Event
	 * @param target ({@code Entity}) the Entity which is being targeted by the
	 *               Event
	 * @return {@code boolean} whether the Event was modified by the Entity's
	 *         Effects
	 */
	public boolean processEvent(Event e, Entity source, Entity target) {
		boolean modifiedEvent = false;
		for (Effect effect : activeEffects) {
			if (effect.processEvent(e, source, target)) {
				modifiedEvent = true;
			}
		}
		return modifiedEvent;
	}

	/**
	 * This function returns the list of Effects active on the Entity
	 * 
	 * @return {@code LinkedList<Effect>} active Effects
	 */
	public LinkedList<Effect> getEffects() {
		return activeEffects;
	}

	/**
	 * This function removes all Effects which have ended from the Entity
	 */
	public void clearEndedEffects() {
		for (int i = 0; i < activeEffects.size(); i++) {
			if (activeEffects.get(i).isEnded()) {
				activeEffects.remove(i);
				i--;
			}
		}
	}

	// ---- Methods concerning entity data ----

	/**
	 * This function allows an Entity to receive a Damage Event and determine its
	 * hit point loss appropriately
	 * 
	 * @param d ({@code Damage}) the Damage being suffered
	 */
	public void receiveDamage(Damage d) {
		System.out.println(this + " received " + d);
		for (DamageDiceGroup group : d.getDamageDice()) {
			int damage = 0;
			if (group.getEffectiveness() == DamageDiceGroup.NORMAL) {
				damage = group.getSum();
				System.out.println(this + " takes " + damage + " " + group.getDamageType() + " damage (bonus: "
						+ group.getBonus() + ")");
			} else if (group.getEffectiveness() == DamageDiceGroup.RESISTED) {
				damage = Math.max(1, group.getSum() / 2);
				System.out.println(this + " takes " + damage + " " + group.getDamageType() + " damage (bonus: "
						+ group.getBonus() + ") (resistant)");
			} else if (group.getEffectiveness() == DamageDiceGroup.ENHANCED) {
				damage = group.getSum() * 2;
				System.out.println(this + " takes " + damage + " " + group.getDamageType() + " damage (bonus: "
						+ group.getBonus() + ") (vulnerable)");
			} else if (group.getEffectiveness() == DamageDiceGroup.NEUTRALIZED) {
				damage = group.getSum();
				System.out.println(this + " takes " + damage + " " + group.getDamageType() + " damage (bonus: "
						+ group.getBonus() + ") (resistant and vulnerable)");
			} else if (group.getEffectiveness() == DamageDiceGroup.NO_EFFECT) {
				System.out.println(this + " takes 0 " + group.getDamageType() + " damage (bonus: " + group.getBonus()
						+ ") (immune)");
			}
			takeDamage(damage);
		}
	}

	/**
	 * This function directly reduces the hit points (or temporary hit points, if >
	 * 0) of the Entity
	 * 
	 * TODO: implement health decrementation from damage
	 * 
	 * @param damage ({@code int}) the quantity of damage to be taken
	 */
	private void takeDamage(int damage) {
	}

	/**
	 * This function returns the modifier for the Entity's ability score of the
	 * passed index
	 * 
	 * @param ability ({@code int}) the index of the ability score in question
	 * @return {@code int} the modifier for the Entity's ability score in question
	 */
	public int getAbilityModifier(int ability) {
		return (abilityScores[ability] - 10) / 2;
	}

	/**
	 * This function returns the proficiency bonus of the Entity
	 * 
	 * @return {@code int} proficiency bonus
	 */
	public int getProficiencyBonus() {
		return ((level - 1) / 4) + 2;
	}

	/**
	 * This function returns the level of the Entity
	 * 
	 * @return {@code int} level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * This function sets the Entity's concentration to the passed effect
	 * 
	 * TODO: make this support multiple-Effect concentration
	 * 
	 * @param e ({@code Effect}) the Effect being concentrated on
	 * @return {@code boolean} whether the Entity was already concentrating
	 */
	public boolean setConcentration(Effect e) {
		if (concentration != null) {
			concentration.end();
			concentration = e;
			return false;
		}
		concentration = e;
		return true;
	}

	/**
	 * This function ends the Effect the Entity is currently concentrating on, and
	 * sets the concentration field to null
	 */
	public void breakConcentration() {
		concentration.end();
		concentration = null;
	}

	/**
	 * This function returns whether the Entity is proficient in any of a passed
	 * list of WeaponTypes
	 * 
	 * @param type ({@code LinkedList<WeaponType>}) the list of weapon types in
	 *             question
	 * @return {@code boolean} whether the Entity is proficient in any of the passed
	 *         WeaponTypes
	 */
	public boolean hasWeaponProficiency(LinkedList<WeaponType> type) {
		for (WeaponType t : type) {
			if (weaponProficiency.contains(t)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This function returns whether the Entity is proficient in saving throws for a
	 * particular ability score
	 * 
	 * @param ability ({@code int}) the index of the ability score in question
	 * @return {@code boolean} whether the Entity is proficient in saving throws for
	 *         the ability score in question
	 */
	public boolean getSaveProficiency(int ability) {
		return saveProficiency[ability];
	}

	/**
	 * This function generates and sets the health fields of the Entity based on its
	 * hit die size, level, and Con score
	 * 
	 * TODO: make hit dice variable field for Entity
	 * 
	 * @param hitDieSize ({@code int}) the size of the Entity's hit dice
	 */
	public void generateHealth(int hitDieSize) {
		DiceGroup dice = new DiceGroup(level, hitDieSize);
		dice.roll();
		health = dice.getSum() + (getAbilityModifier(Entity.CON) * level);
		healthBase = health;
		healthMax = health;
		healthTmp = 0;
	}

	/**
	 * This function returns the Inventory of the Entity
	 * 
	 * TODO: remove once Entity and Inventory are merged
	 * 
	 * @return {@code Inventory} inventory
	 */
	public Inventory getInventory() {
		return inventory;
	}

}
