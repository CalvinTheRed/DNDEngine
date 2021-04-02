package com.dndsuite.core;

import java.util.LinkedList;

import org.luaj.vm2.Varargs;

import com.dndsuite.core.events.Event;
import com.dndsuite.core.events.ItemAttack;
import com.dndsuite.core.gameobjects.Entity;
import com.dndsuite.core.tasks.Task;
import com.dndsuite.dnd.combat.DamageDiceGroup;
import com.dndsuite.dnd.data.DamageType;

public class Item extends Scriptable {

	// Generic Item property tags (for both armor and weapons)
	public static final String HEAVY = "Heavy";
	public static final String LIGHT = "Light";
	// Armor property tags
	public static final String ARMOR = "Armor"; // Permits an item to be equipped as armor
	public static final String ARMOR_HEAVY = "Heavy Armor";
	public static final String ARMOR_LIGHT = "Light Armor";
	public static final String ARMOR_MEDIUM = "Medium Armor";
	public static final String MEDIUM = "Medium";
	public static final String METALLIC = "Metallic"; // Druids will typically not wear metal armor
	public static final String SHIELD = "Shield";
	public static final String STEALTH_DADV = "Stealth Disadvantage";
	// TODO: add item lua function to determine stat prereqs for using item
	// Weapon property tags
	public static final String AMMUNITION = "Ammunition";
	public static final String FINESSE = "Finesse";
	public static final String IMPROVISED_MELEE = "Improvised Melee";
	public static final String IMPROVISED_THROWN = "Improvised Thrown";
	public static final String LOADING = "Loading";
	public static final String MAGICAL = "Magical";
	public static final String RANGED = "Ranged";
	public static final String REACH = "Reach";
	public static final String SILVERED = "Silvered";
	public static final String THROWN = "Thrown";
	public static final String TWO_HANDED = "Two Handed";
	public static final String VERSATILE = "Versatile";
	// Weapon proficiency groups (categorical)
	public static final String MARTIAL_MELEE = "Martial Melee";
	public static final String MARTIAL_RANGED = "Martial Ranged";
	public static final String SIMPLE_MELEE = "Simple Melee";
	public static final String SIMPLE_RANGED = "Simple Ranged";
	// Weapon proficiency groups (particulars)
	public static final String BATTLEAXE = "Battleaxe";
	public static final String BLOWGUN = "Blowgun";
	public static final String CLUB = "Club";
	public static final String DAGGER = "Dagger";
	public static final String DART = "Dart";
	public static final String FLAIL = "Flail";
	public static final String GLAIVE = "Glaive";
	public static final String GREATAXE = "Greataxe";
	public static final String GREATCLUB = "Greatclub";
	public static final String GREATSWORD = "Greatsword";
	public static final String HALBERD = "Halberd";
	public static final String HANDAXE = "Handaxe";
	public static final String HAND_CROSSBOW = "Hand Crossbow";
	public static final String HEAVY_CROSSBOW = "Heavy Crossbow";
	public static final String JAVELIN = "Javelin";
	public static final String LANCE = "Lance";
	public static final String LIGHT_CROSSBOW = "Light Crossbow";
	public static final String LIGHT_HAMMER = "Light Hammer";
	public static final String LONGBOW = "Longbow";
	public static final String LONGSWORD = "Longsword";
	public static final String MACE = "Mace";
	public static final String MAUL = "Maul";
	public static final String MORNINGSTAR = "Morningstar";
	public static final String NET = "Net";
	public static final String PIKE = "Pike";
	public static final String QUARTERSTAFF = "Quarterstaff";
	public static final String RAPIER = "Rapier";
	public static final String SCIMITAR = "Scimitar";
	public static final String SHORTBOW = "Shortbow";
	public static final String SHORTSWORD = "Shortsword";
	public static final String SICKLE = "Sickle";
	public static final String SLING = "Sling";
	public static final String SPEAR = "Spear";
	public static final String TRIDENT = "Trident";
	public static final String WAR_PICK = "War Pick";
	public static final String WARHAMMER = "Warhammer";
	public static final String WHIP = "Whip";

	public Item(String script) {
		super(script);
	}

	public void equip(Entity subject) {
		try {
			passToLua("subject", subject);
			invokeFromLua("equip");
		} catch (Exception ex) {
			
		}
	}

	public int getAC() {
		try {
			Varargs va = invokeFromLua("acbase");
			return va.toint(1);
		} catch (Exception ex) {
			System.out.println("[JAVA] Non-armor item being referenced for AC");
			return 0;
		}
	}

	public int getACAbilityBonusLimit() {
		try {
			Varargs va = invokeFromLua("acAbilityBonusLimit");
			int limit = va.toint(1);
			if (limit == -1) {
				return Integer.MAX_VALUE;
			}
			return limit;
		} catch (Exception ex) {
			return Integer.MAX_VALUE;
		}
	}

	public LinkedList<Event> getItemAttackOptions() {
		LinkedList<Event> events = new LinkedList<Event>();
		
		events.add(new ItemAttack(Entity.STR, this, ItemAttack.MELEE));
		events.add(new ItemAttack(Entity.STR, this, ItemAttack.THROWN));
		
		if (hasTag(Item.FINESSE)) {
			events.add(new ItemAttack(Entity.DEX, this, ItemAttack.MELEE));
			
			if (hasTag(Item.THROWN)) {
				events.add(new ItemAttack(Entity.DEX, this, ItemAttack.THROWN));
			}
		}
		
		if (hasTag(Item.RANGED)) {
			events.add(new ItemAttack(Entity.DEX, this, ItemAttack.RANGED));
		}
		
		return events;
	}

	public LinkedList<Task> getCustomTasks() {
		try {
			Varargs va = invokeFromLua("customTasks");
			LinkedList<Task> tasks = new LinkedList<Task>();
			int index = 1;
			Task task = (Task) (va.touserdata(index));
			while (task != null) {
				tasks.add(task);
				index++;
				task = (Task) (va.touserdata(index));
			}
			return tasks;
		} catch (Exception ex) {
			return new LinkedList<Task>();
		}
	}

	public DamageDiceGroup getDamageDice(String attackType) {
		try {
			passToLua("attackType", attackType);
			Varargs va = invokeFromLua("damage");
			return (DamageDiceGroup) va.touserdata(1);
		} catch (Exception ex) {
			return new DamageDiceGroup(0,0,DamageType.TYPELESS);
		}
	}

	public double[] getRange(String attackType) {
		try {
			passToLua("attackType", attackType);
			Varargs va = invokeFromLua("range");
			double[] range = new double[2];
			range[0] = va.todouble(1);
			range[1] = va.todouble(2);
			return range;
		} catch (Exception ex) {
			return new double[] { 0.0, 0.0 };
		}
	}

	public double getReach() {
		try {
			Varargs va = invokeFromLua("reach");
			return va.todouble(1);
		} catch (Exception ex) {
			return 0.0;
		}
	}

	public double getWeight() {
		try {
			Varargs va = invokeFromLua("weight");
			return va.todouble(1);
		} catch (Exception ex) {
			return 0.0;
		}
	}

	public void unequip(Entity subject) {
		try {
			passToLua("subject", subject);
			invokeFromLua("unequip");
		} catch (Exception ex) {
			
		}
	}

}
