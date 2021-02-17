package dnd.items;

import java.util.LinkedList;

import dnd.combat.DamageDiceGroup;
import dnd.data.ArmorProperty;
import dnd.data.DamageType;
import dnd.data.WeaponProperty;
import dnd.events.Event;
import dnd.events.dicecontests.attackroll.WeaponAttack;
import gameobjects.entities.Entity;

public abstract class Item {
	public static final double STD_REACH = 5.0;
	public static final double[] STD_RANGE = {20.0, 60.0};
	
	protected String name;
	protected double[] range;
	protected double reach;
	protected LinkedList<ArmorProperty> armorProperties;
	protected LinkedList<WeaponProperty> weaponProperties;
	
	public Item(String name, double[] range, double reach) {
		this.name = name;
		this.range = range;
		this.reach = reach;
		armorProperties = new LinkedList<ArmorProperty>();
		weaponProperties = new LinkedList<WeaponProperty>();
	}
	
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
	
	public double[] getRange() {
		return range;
	}
	
	public double getReach() {
		return reach;
	}
	
	public boolean hasArmorProperty(ArmorProperty p) {
		return armorProperties.contains(p);
	}
	
	public boolean hasWeaponProperty(WeaponProperty p) {
		return weaponProperties.contains(p);
	}
	
	public DamageDiceGroup getDamageDice() {
		return new DamageDiceGroup(1, 4, DamageType.BLUDGEONING);
	}
	
}
