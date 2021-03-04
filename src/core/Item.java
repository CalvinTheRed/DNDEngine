package core;

import java.util.LinkedList;

import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import core.events.AttackRoll;
import core.events.Event;
import core.events.WeaponAttack;
import core.gameobjects.Entity;
import core.tasks.Task;
import dnd.combat.DamageDiceGroup;

public class Item extends Scriptable {

	// Generic Item property tags (for both armor and weapons)
	public static final String HEAVY = "Heavy";
	public static final String LIGHT = "Light";
	// Armor property tags
	public static final String ARMOR = "Armor";
	public static final String MEDIUM = "Medium";
	public static final String METALLIC = "Metallic";
	public static final String SHIELD = "Shield";
	public static final String STEALTH_DADV = "Stealth Disadvantage";
	public static final String STR_13 = "STR 13";
	public static final String STR_15 = "STR 15";
	// Weapon property tags
	public static final String AMMUNITION = "Ammunition";
	public static final String FINESSE = "Finesse";
	public static final String IMPROVISED_MELEE = "Improvised Melee";
	public static final String IMPROVISED_THROWN = "Improvised Thrown";
	public static final String LOADING = "Loading";
	public static final String MAGICAL = "Magical";
	public static final String RANGE = "Range";
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

	protected String name;
	protected LinkedList<String> tags;

	public Item(String script) {
		super(script);
		tags = new LinkedList<String>();
		globals.set("item", CoerceJavaToLua.coerce(this));
		globals.get("define").invoke();
	}

	public void equip(Entity subject) {
		globals.set("subject", CoerceJavaToLua.coerce(subject));
		globals.get("equip").invoke();
	}

	public LinkedList<Event> getMainhandAttackOptions() {
		LinkedList<Event> events = new LinkedList<Event>();

		events.add(new WeaponAttack(this, Entity.STR, WeaponAttack.MELEE, true));
		events.add(new WeaponAttack(this, Entity.STR, WeaponAttack.THROWN, true));

		if (hasTag(FINESSE)) {
			events.add(new WeaponAttack(this, Entity.DEX, WeaponAttack.MELEE, true));
			if (hasTag(THROWN)) {
				events.add(new WeaponAttack(this, Entity.DEX, WeaponAttack.THROWN, true));
			}
		}

		if (hasTag(RANGE)) {
			events.add(new WeaponAttack(this, Entity.DEX, WeaponAttack.RANGED, true));
		}

		return events;
	}

	public LinkedList<Task> getCustomTasks() {
		Varargs va = globals.get("customTasks").invoke();
		LinkedList<Task> tasks = new LinkedList<Task>();
		int index = 1;
		Task task = (Task) (va.touserdata(index));
		while (task != null) {
			tasks.add(task);
			index++;
			task = (Task) (va.touserdata(index));
		}
		return tasks;
	}

	public DamageDiceGroup getDamageDice() {
		Varargs va = globals.get("damage").invoke();
		return (DamageDiceGroup) (va.touserdata(1, DamageDiceGroup.class));
	}

	public double[] getRange(String attackType) {
		double[] range = new double[2];
		Varargs va = globals.get("range").invoke();
		if (attackType == AttackRoll.RANGED && hasTag(RANGE)) {
			range[Event.SHORTRANGE] = va.todouble(3);
			range[Event.LONGRANGE] = va.todouble(4);
		} else {
			range[Event.SHORTRANGE] = va.todouble(1);
			range[Event.LONGRANGE] = va.todouble(2);
		}
		return range;
	}

	public double getReach() {
		Varargs va = globals.get("reach").invoke();
		return va.todouble(1);
	}

	public double getWeight() {
		Varargs va = globals.get("weight").invoke();
		return va.todouble(1);
	}

	public void unequip(Entity subject) {
		globals.set("subject", CoerceJavaToLua.coerce(subject));
		globals.get("unequip").invoke();
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addTag(String tag) {
		tags.add(tag);
	}

	public boolean hasTag(String tag) {
		return tags.contains(tag);
	}

	@Override
	public String toString() {
		return name;
	}

}
