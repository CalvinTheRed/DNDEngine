package com.dndsuite.core.gameobjects;

import java.util.LinkedList;

import com.dndsuite.core.Item;
import com.dndsuite.core.Observer;
import com.dndsuite.core.Subject;
import com.dndsuite.core.effects.Effect;
import com.dndsuite.core.effects.ItemProficiencyWatcher;
import com.dndsuite.core.effects.SaveProficiencyWatcher;
import com.dndsuite.core.effects.SpellProficiencyWatcher;
import com.dndsuite.core.events.Damage;
import com.dndsuite.core.events.Event;
import com.dndsuite.core.events.TaskCollection;
import com.dndsuite.core.events.groups.EventGroup;
import com.dndsuite.core.tasks.Task;
import com.dndsuite.dnd.combat.DamageDiceGroup;
import com.dndsuite.maths.Vector;
import com.dndsuite.maths.dice.DiceGroup;

/**
 * An abstract class which represents a game piece which has the ability to
 * perform actions
 * 
 * @author calvi
 *
 */
public abstract class Entity extends GameObject implements Subject {

	public static final int STR = 0;
	public static final int DEX = 1;
	public static final int CON = 2;
	public static final int INT = 3;
	public static final int WIS = 4;
	public static final int CHA = 5;

	protected LinkedList<Observer> observers;
	protected LinkedList<Effect> activeEffects;
	protected LinkedList<EventGroup> eventQueue;
	protected LinkedList<Task> availableTasks;
	protected LinkedList<Task> baseTasks;
	protected LinkedList<Item> inventory;
	protected LinkedList<String> itemProficiencies;

	protected Item mainhand;
	protected Item offhand;
	protected Item armor;

	protected int experience;
	protected int health;
	protected int healthBase;
	protected int healthMax;
	protected int healthTmp;
	protected int level;
	protected int[] abilityScores;
	protected int[] baseAbilityScores;

	public Entity(String script, Vector pos, Vector rot) {
		super(script, pos, rot);

		abilityScores = new int[6];
		baseAbilityScores = new int[6];
		observers = new LinkedList<Observer>();
		activeEffects = new LinkedList<Effect>();
		eventQueue = new LinkedList<EventGroup>();
		availableTasks = new LinkedList<Task>();
		baseTasks = new LinkedList<Task>();
		inventory = new LinkedList<Item>();
		itemProficiencies = new LinkedList<String>();

		addEffect(new ItemProficiencyWatcher(this));
		addEffect(new SpellProficiencyWatcher(this));
		addEffect(new SaveProficiencyWatcher(this));
	}

	public boolean addBaseTask(Task task) {
		if (!baseTasks.contains(task)) {
			baseTasks.add(task);
			return true;
		}
		return false;
	}

	public boolean addEffect(Effect e) {
		if (activeEffects.contains(e)) {
			return false;
		}
		activeEffects.add(e);
		System.out.println("[JAVA] " + this + " given effect " + e);
		return true;
	}

	public void addItemProficiency(String proficiencyGroup) {
		itemProficiencies.add(proficiencyGroup);
	}

	public void addObserver(Observer o) {
		observers.add(o);
	}

	public boolean addTask(Task task) {
		if (!availableTasks.contains(task)) {
			availableTasks.add(task);
			return true;
		}
		return false;
	}

	public void clearEndedEffects() {
		for (int i = 0; i < activeEffects.size(); i++) {
			if (activeEffects.get(i).isEnded()) {
				activeEffects.remove(i);
				i--;
			}
		}
	}

	public void clearEventQueue() {
		eventQueue.clear();
	}

	public void equipArmor(Item item) {
		if (!inventory.contains(item)) {
			inventory.add(item);
		}
		if (item.hasTag(Item.ARMOR)) {
			armor = item;
			updateObservers();
		} else {
			System.out.println("[JAVA] ERR: " + item + " not armor");
		}
	}

	public void equipMainhand(Item item) {
		stowMainhand();
		if (!inventory.contains(item)) {
			inventory.add(item);
		}
		mainhand = item;
		updateObservers();
		if (item.hasTag(Item.TWO_HANDED)) {
			offhand = item;
		}
		item.equip(this);
	}

	public void equipOffhand(Item item) {
		stowOffhand();
		if (!inventory.contains(item)) {
			inventory.add(item);
		}
		offhand = item;
		updateObservers();
		if (item.hasTag(Item.TWO_HANDED)) {
			mainhand = item;
		}
		item.equip(this);
	}

	public void generateHealth(int hitDieSize) {
		DiceGroup dice = new DiceGroup(level, hitDieSize);
		dice.roll();
		health = dice.getSum() + (getAbilityModifier(Entity.CON) * level);
		healthBase = health;
		healthMax = health;
		healthTmp = 0;
	}

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

	public int getAbilityModifier(int ability) {
		int abilityScoreBuffer = abilityScores[ability] - 10;
		if (abilityScoreBuffer < 0) {
			abilityScoreBuffer--;
		}
		return abilityScoreBuffer / 2;
	}

	public Item getArmor() {
		return armor;
	}

	public LinkedList<Effect> getEffects() {
		return activeEffects;
	}

	public LinkedList<EventGroup> getEventQueue() {
		return eventQueue;
	}

	public LinkedList<Item> getInventory() {
		return inventory;
	}

	public int getLevel() {
		return level;
	}

	public Item getMainhand() {
		return mainhand;
	}

	public Item getOffhand() {
		return offhand;
	}

	public int getProficiencyBonus() {
		return ((level - 1) / 4) + 2;
	}

	public LinkedList<Task> getTasks() {
		TaskCollection tc = new TaskCollection(this);
		tc.addTasks(baseTasks);
		try {
			tc.addTasks(getMainhand().getCustomTasks());
		} catch (NullPointerException ex) {
		}
		try {
			tc.addTasks(getOffhand().getCustomTasks());
		} catch (NullPointerException ex) {
		}
		try {
			tc.addTasks(getArmor().getCustomTasks());
		} catch (NullPointerException ex) {
		}
		processEvent(tc, this, this);
		availableTasks.clear();
		availableTasks.addAll(tc.getTasks());
		return availableTasks;
	}

	public boolean invokeQueuedEvent(int groupIndex, int eventIndex, Vector targetPos) {
		if (eventQueue.size() > groupIndex && eventQueue.get(groupIndex).getEvents().size() > eventIndex) {
			eventQueue.get(groupIndex).getEvents().get(eventIndex).invoke(this, targetPos);
			eventQueue.remove(groupIndex);
			return true;
		}
		return false;
	}

	public boolean invokeTask(int index) {
		if (availableTasks.size() > index) {
			return availableTasks.get(index).invoke(this);
		}
		return false;
	}

	public boolean processEvent(Event e, Entity source, Entity target) {
		for (Effect effect : activeEffects) {
			effect.processEvent(e, source, target);
		}
		return false;
	}

	public boolean proficientWith(String proficiencyGroup) {
		return proficiencyGroup == null || itemProficiencies.contains(proficiencyGroup);
	}

	public boolean queueEventGroup(EventGroup group) {
		if (!eventQueue.contains(group)) {
			eventQueue.add(group);
			return true;
		}
		return false;
	}

	public void receiveDamage(Damage d) {
		System.out.println("[JAVA] " + this + " received " + d);
		for (DamageDiceGroup group : d.getDamageDice()) {
			int damage = 0;
			if (group.getEffectiveness() == DamageDiceGroup.NORMAL) {
				damage = group.getSum();
				System.out.println("[JAVA] " + this + " takes " + damage + " " + group.getDamageType()
						+ " damage (bonus: " + group.getBonus() + ")");
			} else if (group.getEffectiveness() == DamageDiceGroup.RESISTED) {
				damage = Math.max(1, group.getSum() / 2);
				System.out.println("[JAVA] " + this + " takes " + damage + " " + group.getDamageType()
						+ " damage (bonus: " + group.getBonus() + ") (resistant)");
			} else if (group.getEffectiveness() == DamageDiceGroup.ENHANCED) {
				damage = group.getSum() * 2;
				System.out.println(this + " takes " + damage + " " + group.getDamageType() + " damage (bonus: "
						+ group.getBonus() + ") (vulnerable)");
			} else if (group.getEffectiveness() == DamageDiceGroup.NEUTRALIZED) {
				damage = group.getSum();
				System.out.println("[JAVA] " + this + " takes " + damage + " " + group.getDamageType()
						+ " damage (bonus: " + group.getBonus() + ") (resistant and vulnerable)");
			} else if (group.getEffectiveness() == DamageDiceGroup.NO_EFFECT) {
				System.out.println("[JAVA] " + this + " takes 0 " + group.getDamageType() + " damage (bonus: "
						+ group.getBonus() + ") (immune)");
			}
			takeDamage(damage);
		}
	}

	public void removeObserver(Observer o) {
		observers.remove(o);
	}

	public void stowMainhand() {
		if (mainhand != null) {
			if (mainhand.hasTag(Item.TWO_HANDED)) {
				offhand = null;
			}
			mainhand = null;
		}
	}

	public void stowOffhand() {
		if (offhand != null) {
			if (offhand.hasTag(Item.TWO_HANDED)) {
				mainhand = null;
			}
			offhand = null;
		}
	}

	private void takeDamage(int damage) {
		// TODO: implement health decrementation here
	}

	public void updateObservers() {
		for (Observer o : observers) {
			o.update(this);
		}
	}

	public void versatileSet() {
		if (mainhand.hasTag(Item.VERSATILE) && offhand == null) {
			offhand = mainhand;
		}
	}

	public void versatileUnset() {
		if (mainhand.hasTag(Item.VERSATILE) && offhand == mainhand) {
			offhand = null;
		}
	}

}
