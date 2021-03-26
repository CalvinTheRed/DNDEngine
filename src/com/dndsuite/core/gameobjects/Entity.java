package com.dndsuite.core.gameobjects;

import java.util.LinkedList;

import com.dndsuite.core.Item;
import com.dndsuite.core.effects.Effect;
import com.dndsuite.core.events.TaskCollection;
import com.dndsuite.core.events.groups.EventGroup;
import com.dndsuite.core.tasks.Task;
import com.dndsuite.maths.Vector;

public class Entity extends GameObject {
	public static final String ABERRATION = "Aberration";
	public static final String BEAST = "Beast";
	public static final String CELESTIAL = "Celestial";
	public static final String CONSTRUCT = "Construct";
	public static final String DRAGON = "Dragon";
	public static final String ELEMENTAL = "Elemental";
	public static final String FEY = "Fey";
	public static final String FIEND = "Fiend";
	public static final String GIANT = "Giant";
	public static final String HUMANOID = "Humanoid";
	public static final String MONSTROSITY = "Monstrosity";
	public static final String OOZE = "Ooze";
	public static final String PLANT = "Plant";
	public static final String UNDEAD = "Undead";

	public static final int STR = 0;
	public static final int DEX = 1;
	public static final int CON = 2;
	public static final int INT = 3;
	public static final int WIS = 4;
	public static final int CHA = 5;

	protected LinkedList<EventGroup> eventQueue;
	protected LinkedList<Task> availableTasks;
	protected LinkedList<Task> baseTasks;
	protected LinkedList<String> itemProficiencies;

	protected Item mainhand;
	protected Item offhand;
	protected Item armor;

	protected int experience;
	protected int level;
	protected int[] abilityScores;
	protected int[] baseAbilityScores;

	public Entity(String script, Vector pos, Vector rot) {
		super(script, pos, rot);
		addTag(Entity.getGameObjectID());
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

	public boolean addTask(Task task) {
		if (!availableTasks.contains(task)) {
			availableTasks.add(task);
			return true;
		}
		return false;
	}

	public void clearEventQueue() {
		eventQueue.clear();
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

	public static String getGameObjectID() {
		return "Entity";
	}

	/*
	 * -----------------------------------------------------------------------------
	 * Functions used from Lua define function -------------------------------------
	 * -----------------------------------------------------------------------------
	 */

	public void prepEntity() {
		eventQueue = new LinkedList<EventGroup>();
		availableTasks = new LinkedList<Task>();
		baseTasks = new LinkedList<Task>();
		itemProficiencies = new LinkedList<String>();
		abilityScores = new int[6];
		baseAbilityScores = new int[6];
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setExperience(int experience) {
		this.experience = experience;
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

	public void equipArmor(Item item) {
		if (!inventory.contains(item)) {
			addToInventory(item);
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
			addToInventory(item);
		}
		mainhand = item;
		if (item.hasTag(Item.TWO_HANDED)) {
			offhand = item;
		}
		item.equip(this);
		updateObservers();
	}

	public void equipOffhand(Item item) {
		stowOffhand();
		if (!inventory.contains(item)) {
			addToInventory(item);
		}
		offhand = item;
		if (item.hasTag(Item.TWO_HANDED)) {
			mainhand = item;
		}
		item.equip(this);
		updateObservers();
	}

	// TODO: find a more elegant solution to this
	public void setAbilityScores(int[] abilityScores) {
		for (int i = 0; i < this.abilityScores.length; i++) {
			this.abilityScores[i] = abilityScores[i];
		}
	}

	// TODO: find a more elegant solution to this
	public void setBaseAbilityScores(int[] baseAbilityScores) {
		for (int i = 0; i < this.baseAbilityScores.length; i++) {
			this.baseAbilityScores[i] = baseAbilityScores[i];
		}
	}

}
