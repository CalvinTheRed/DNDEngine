package dnd.items;

import java.util.LinkedList;

import dnd.combat.DamageDiceGroup;
import dnd.data.ArmorProperty;
import dnd.data.DamageType;
import dnd.data.WeaponProperty;
import dnd.events.Event;
import dnd.events.dicecontests.attackroll.WeaponAttack;
import gameobjects.entities.Entity;

/**
 * A class representing a physical object which canbe worn or carried
 * 
 * @author calvi
 *
 */
public abstract class Item {
	public static final double STD_REACH = 5.0;
	public static final double[] STD_RANGE = { 20.0, 60.0 };

	protected String name;
	protected double[] range;
	protected double reach;
	protected LinkedList<ArmorProperty> armorProperties;
	protected LinkedList<WeaponProperty> weaponProperties;

	/**
	 * The specific constructoir for class Item
	 * 
	 * @param name  ({@code String}) the name of the Item
	 * @param range ({@code double[2]}) the short and long range of the Item, if
	 *              used as a weapon
	 * @param reach ({@code double}) the reach of the item, if used as a weapon
	 */
	public Item(String name, double[] range, double reach) {
		this.name = name;
		this.range = range;
		this.reach = reach;
		armorProperties = new LinkedList<ArmorProperty>();
		weaponProperties = new LinkedList<WeaponProperty>();
	}

	/**
	 * The general-purpose constructor for class Item
	 * 
	 * @param name ({@code String}) the name of the Item
	 */
	public Item(String name) {
		this.name = name;
		range = STD_RANGE;
		reach = STD_REACH;
		armorProperties = new LinkedList<ArmorProperty>();
		weaponProperties = new LinkedList<WeaponProperty>();
	}

	@Override
	public String toString() {
		return name;
	}

	/**
	 * This function returns a list of Events which may be taken if this Item is
	 * attempted to be used as a weapon, as determined by the weapon properties of
	 * the Item
	 * 
	 * @return {@code LinkedList<Event>} weapon attack event options
	 */
	public LinkedList<Event> getAttackOptions() {
		LinkedList<Event> events = new LinkedList<Event>();

		events.add(new WeaponAttack(this, WeaponAttack.MELEE, Entity.STR));
		events.add(new WeaponAttack(this, WeaponAttack.THROWN, Entity.STR));

		if (hasWeaponProperty(WeaponProperty.FINESSE)) {
			events.add(new WeaponAttack(this, WeaponAttack.MELEE, Entity.DEX));
			if (hasWeaponProperty(WeaponProperty.THROWN)) {
				events.add(new WeaponAttack(this, WeaponAttack.THROWN, Entity.DEX));
			}
		}

		if (hasWeaponProperty(WeaponProperty.RANGE)) {
			events.add(new WeaponAttack(this, WeaponAttack.RANGED, Entity.DEX));
		}

		return events;
	}

	/**
	 * This function returns the short and long range of the Item, if used as a
	 * weapon
	 * 
	 * @return {@code double[2]} range
	 */
	public double[] getRange() {
		return range;
	}

	/**
	 * This function returns the reach of the Item, if used as a weapon
	 * 
	 * @return {@code double} reach
	 */
	public double getReach() {
		return reach;
	}

	/**
	 * This function returns whether this Item has a particular armor property
	 * 
	 * @param p ({@code ArmorProperty}) the property being searched for
	 * @return {@code boolean} whether the Item possesses the passed armor property
	 */
	public boolean hasArmorProperty(ArmorProperty p) {
		return armorProperties.contains(p);
	}

	/**
	 * This function returns whether this Item has a particular weapon property
	 * 
	 * @param p ({@code WeaponProperty}) the property being searched for
	 * @return {@code boolean} whether the Item possesses the passed weapon property
	 */
	public boolean hasWeaponProperty(WeaponProperty p) {
		return weaponProperties.contains(p);
	}

	/**
	 * This function returns the DamageDiceGroup assigned to the Item, if used as a
	 * weapon. All Items deal 1d4 bludgeoning damage if used as a weapon unless
	 * otherwise specified
	 * 
	 * @return {@code DamageDiceGroup} the Item's damage die/dice
	 */
	public DamageDiceGroup getDamageDice() {
		return new DamageDiceGroup(1, 4, DamageType.BLUDGEONING);
	}

}
