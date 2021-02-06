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
import dnd.items.Inventory;
import engine.effects.Effect;
import engine.events.Damage;
import engine.events.Event;
import gameobjects.GameObject;
import maths.Vector;
import maths.dice.DiceGroup;

public abstract class Entity extends GameObject {
	
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
	
	protected int baseArmorClass;
	protected int experience;
	protected int health;
	protected int healthBase;
	protected int healthMax;
	protected int healthTmp;
	protected int level;
	
	protected Alignment    alignment;
	protected CreatureType type;
	protected SizeCategory size;
	
	protected int[]     abilityScores;
	protected int[]     baseAbilityScores;
	protected boolean[] saveProficiency;
	protected boolean[] skillExpertise;
	protected boolean[] skillProficiency;
	
	protected Effect    concentration;
	protected Inventory inventory;
	
	protected LinkedList<Condition>  conditionImmunities;
	protected LinkedList<DamageType> immunities;
	protected LinkedList<DamageType> resistances;
	protected LinkedList<DamageType> vulnerabilities;
	protected LinkedList<Effect>     observedEffects;
	protected LinkedList<Entity>     targets;
	protected LinkedList<Event>      invokableEvents;
	protected LinkedList<Event>      stagedEvents; // TODO: implement stagedEvents
	protected LinkedList<Language>   languages;
	protected LinkedList<WeaponType> weaponProficiency;
	
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
		invokableEvents     = new LinkedList<Event>();
		stagedEvents        = new LinkedList<Event>();
		languages           = new LinkedList<Language>();
		weaponProficiency   = new LinkedList<WeaponType>();
	}
	
	public boolean addTarget(Entity e) {
		if (targets.contains(e)) {
			return false;
		}
		targets.add(e);
		return true;
	}
	
	public boolean removeTarget(Entity e) {
		return targets.remove(e);
	}
	
	public void clearTargets() {
		targets.clear();
	}
	
	public LinkedList<Event> getInvokableEvents(){
		return invokableEvents;
	}
	
	public boolean invokeEvent(Event e) {
		if (invokableEvents.contains(e)) {
			e.invoke(targets);
			return true;
		}
		return false;
	}
	
	public boolean observeEffect(Effect e) {
		if (observedEffects.contains(e)) {
			return false;
		}
		observedEffects.add(e);
		System.out.println(this + " now observing " + e);
		return true;
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
	
	public boolean processEvent(Event e, Entity target) {
		boolean modifiedEvent = false;
		for (Effect effect : observedEffects) {
			if (effect.processEvent(e, target)) {
				modifiedEvent = true;
			}
		}
		return modifiedEvent;
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
	
	// TODO: write better armor class algorithm
	public int getArmorClass() {
		return baseArmorClass + getAbilityModifier(DEX);
	}
	
	// TODO: write better save DC algorithm
	public int getSaveDiceCheck(int ability) {
		return 8 + getProficiencyBonus() + getAbilityModifier(ability);
	}
	
	public boolean grantWeaponProficiency(WeaponType type) {
		if (weaponProficiency.contains(type)) {
			return false;
		}
		weaponProficiency.add(type);
		return true;
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
	
	public void processDamageEvent(Damage d) {
		for (DamageDiceGroup group : d.getDamageDice()) {
			int damage = group.getSum();
			if (vulnerabilities.contains(group.getDamageType())) {
				damage *= 2;
			}
			if (resistances.contains(group.getDamageType())) {
				damage /= 2;
			}
			if (!immunities.contains(group.getDamageType())) {
				takeDamage(damage);
			}
		}
	}
	
	private void takeDamage(int damage) {
		System.out.println(this + " takes " + damage + " damage!");
		if (healthTmp > 0) {
			if (damage > healthTmp) {
				damage -= healthTmp;
				healthTmp = 0;
				health -= damage;
			}
			else {
				healthTmp -= damage;
			}
		}
		else {
			health -= damage;
		}
		
		if (health < 1) {
			// TODO: initiate death if NPC or saving throw mode if PC
		}
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
}
