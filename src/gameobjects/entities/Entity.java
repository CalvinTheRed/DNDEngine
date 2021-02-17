package gameobjects.entities;

import java.util.LinkedList;

import dnd.combat.DamageDiceGroup;
import dnd.data.Alignment;
import dnd.data.Condition;
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
	
	public static final int ACROBATICS      = 0;
	public static final int ANIMAL_HANDLING = 1;
	public static final int ARCANA          = 2;
	public static final int ATHLETICS       = 3;
	public static final int DECEPTION       = 4;
	public static final int HISTORY         = 5;
	public static final int INSIGHT         = 6;
	public static final int INTIMIDATION    = 7;
	public static final int INVESTIGATION   = 8;
	public static final int MEDICINE        = 9;
	public static final int NATURE          = 10;
	public static final int PERCEPTION      = 11;
	public static final int PERFORMANCE     = 12;
	public static final int PERSUASION      = 13;
	public static final int RELIGION        = 14;
	public static final int SLEIGHT_OF_HAND = 15;
	public static final int STEALTH         = 16;
	public static final int SURVIVAL        = 17;
	
	// ---- Structural variables ----
	
	protected LinkedList<Effect>     observedEffects;
	protected LinkedList<Entity>     targets;
	protected LinkedList<EventGroup> eventQueue;
	
	// ---- Entity data ----
	
	protected int                    baseArmorClass;
	protected int                    experience;
	protected int                    health;
	protected int                    healthBase;
	protected int                    healthMax;
	protected int                    healthTmp;
	protected int                    level;
	protected int[]                  abilityScores;
	protected int[]                  baseAbilityScores;
	protected boolean[]              saveProficiency;
	protected boolean[]              skillExpertise;
	protected boolean[]              skillProficiency;
	protected Alignment              alignment;
	protected CreatureType           type;
	protected SizeCategory           size;
	protected Effect                 concentration; // TODO: concentrating effects need to be able to support multiple targets at a time
	protected Inventory              inventory;
	protected LinkedList<Condition>  conditionImmunities;
	protected LinkedList<DamageType> immunities;
	protected LinkedList<DamageType> resistances;
	protected LinkedList<DamageType> vulnerabilities;
	protected LinkedList<Language>   languages;
	protected LinkedList<Task>       availableTasks;
	protected LinkedList<Task>       baseTasks;
	protected LinkedList<WeaponType> weaponProficiency;
	
	// ---- Constructors ----
	
	public Entity(String name, Vector pos, Vector rot) {
		super(name, pos, rot);
		this.name = name;
		
		abilityScores       = new int[6];
		baseAbilityScores   = new int[6];
		saveProficiency     = new boolean[18];
		skillProficiency    = new boolean[18];
		skillExpertise      = new boolean[18];
		inventory           = new Inventory();
		conditionImmunities = new LinkedList<Condition>();
		immunities          = new LinkedList<DamageType>();
		resistances         = new LinkedList<DamageType>();
		vulnerabilities     = new LinkedList<DamageType>();
		observedEffects     = new LinkedList<Effect>();
		targets             = new LinkedList<Entity>();
		eventQueue          = new LinkedList<EventGroup>();
		languages           = new LinkedList<Language>();
		availableTasks      = new LinkedList<Task>();
		baseTasks           = new LinkedList<Task>();
		weaponProficiency   = new LinkedList<WeaponType>();
	}
	
	// ---- Static class methods ----
	
	public static String getAbility(int abilityIndex) {
		if (abilityIndex == STR) {
			return "STR";
		}
		else if (abilityIndex == DEX) {
			return "DEX";
		}
		else if (abilityIndex == CON) {
			return "CON";
		}
		else if (abilityIndex == INT) {
			return "INT";
		}
		else if (abilityIndex == WIS) {
			return "WIS";
		}
		else if (abilityIndex == CHA) {
			return "CHA";
		}
		return "";
	}
	
	// ---- Methods concerning targets ----
	
	public boolean addTarget(Entity e) {
		if (targets.contains(e)) {
			return false;
		}
		targets.add(e);
		return true;
	}
	
	public void addTargets(LinkedList<Entity> list) {
		for (Entity e : list) {
			if (!targets.contains(e)) {
				targets.add(e);
			}
		}
	}
	
	public boolean removeTarget(Entity e) {
		return targets.remove(e);
	}
	
	public void clearTargets() {
		targets.clear();
	}
	
	// ---- Methods concerning tasks ----
	
	public boolean addBaseTask(Task task) {
		if (!baseTasks.contains(task)) {
			baseTasks.add(task);
			return true;
		}
		return false;
	}
	
	public boolean addTask(Task task) {
		if (!availableTasks.contains(task)) {
			availableTasks.add(task);
			return true;
		}
		return false;
	}
	
	public LinkedList<Task> getTasks(){
		return availableTasks;
	}
	
	public void resetTasks() {
		availableTasks.clear();
		availableTasks.addAll(baseTasks);
	}
	
	public boolean invokeTask(int index) {
		if (availableTasks.size() > index) {
			return availableTasks.get(index).invoke(this);
		}
		return false;
	}

	// ---- Methods concerning the event queue ----
	
	public boolean addToEventQueue(EventGroup group) {
		if (!eventQueue.contains(group)) {
			eventQueue.add(group);
			return true;
		}
		return false;
	}
	
	public LinkedList<EventGroup> getEventQueue(){
		return eventQueue;
	}
	
	public void clearEventQueue() {
		eventQueue.clear();
	}
	
	public boolean invokeQueuedEvent(int groupIndex, int eventIndex, Vector targetPos) {
		if (eventQueue.size() > groupIndex && eventQueue.get(groupIndex).getEvents().size() > eventIndex) {
			eventQueue.get(groupIndex).getEvents().get(eventIndex).invoke(this, targetPos);
			eventQueue.remove(groupIndex);
			return true;
		}
		return false;
	}
	
	// ---- Methods concerning events ----
	
	public boolean observeEffect(Effect e) {
		if (observedEffects.contains(e)) {
			return false;
		}
		observedEffects.add(e);
		System.out.println(this + " now observing " + e);
		return true;
	}
	
	public boolean processEvent(Event e, Entity source, Entity target) {
		boolean modifiedEvent = false;
		for (Effect effect : observedEffects) {
			if (effect.processEvent(e, source, target)) {
				modifiedEvent = true;
			}
		}
		return modifiedEvent;
	}
	
	public LinkedList<Effect> getObservedEffects(){
		return observedEffects;
	}
	
	public void clearEndedEffects() {
		for (int i = 0; i < observedEffects.size(); i++) {
			if (observedEffects.get(i).isEnded()) {
				observedEffects.remove(i);
				i--;
			}
		}
	}
	
	// ---- Methods concerning entity data ----
	
	public void receiveDamage(Damage d) {
		System.out.println(this + " received " + d);
		for (DamageDiceGroup group : d.getDamageDice()) {
			int damage = 0;
			if (group.getEffectiveness() == DamageDiceGroup.NORMAL) {
				damage = group.getSum();
				System.out.println(this + " takes " + damage + " " + group.getDamageType() + " damage (bonus: " + group.getDamageBonus() + ")");
			}
			else if (group.getEffectiveness() == DamageDiceGroup.RESISTED) {
				damage = Math.max(1, group.getSum() / 2);
				System.out.println(this + " takes " + damage + " " + group.getDamageType() + " damage (bonus: " + group.getDamageBonus() + ") (resistant)");
			}
			else if (group.getEffectiveness() == DamageDiceGroup.ENHANCED) {
				damage = group.getSum() * 2;
				System.out.println(this + " takes " + damage + " " + group.getDamageType() + " damage (bonus: " + group.getDamageBonus() + ") (vulnerable)");
			}
			else if (group.getEffectiveness() == DamageDiceGroup.NEUTRALIZED) {
				damage = group.getSum();
				System.out.println(this + " takes " + damage + " " + group.getDamageType() + " damage (bonus: " + group.getDamageBonus() + ") (resistant and vulnerable)");
			}
			else if (group.getEffectiveness() == DamageDiceGroup.NO_EFFECT) {
				System.out.println(this + " takes 0 " + group.getDamageType() + " damage (bonus: " + group.getDamageBonus() + ") (immune)");
			}
			takeDamage(damage);
		}
	}
	
	private void takeDamage(int damage) {
		// TODO: implement health decrementation from damage
	}
	
	public int getAbilityModifier(int ability) {
		return (abilityScores[ability] - 10) / 2;
	}
	
	public int getProficiencyBonus() {
		return ((level - 1) / 4) + 2;
	}
	
	public int getLevel() {
		return level;
	}
	
	public boolean setConcentration(Effect e) {
		if (concentration != null) {
			concentration.end();
			concentration = e;
			return true;
		}
		concentration = e;
		return false;
	}
	
	public void breakConcentration() {
		concentration.end();
		concentration = null;
	}
	
	public boolean hasWeaponProficiency(LinkedList<WeaponType> type) {
		boolean tmp = false;
		for (WeaponType t : type) {
			if (weaponProficiency.contains(t)) {
				tmp = true;
			}
		}
		return tmp;
	}
	
	public boolean getSaveProficiency(int ability) {
		return saveProficiency[ability];
	}
	
	public void generateHealth(int hitDieSize) {
		DiceGroup dice = new DiceGroup(level, hitDieSize);
		dice.roll();
		health = dice.getSum() + (getAbilityModifier(Entity.CON) * level);
		healthBase = health;
		healthMax = health;
		healthTmp = 0;
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
}
