package dnd.items;

import java.util.LinkedList;

import dnd.combat.DamageDiceGroup;
import dnd.data.DamageType;
import dnd.data.WeaponProperty;
import dnd.data.WeaponType;

public abstract class Item {
	protected String name;
	protected int enchantmentBonus;
	protected boolean isMagical;
	protected boolean isSilvered;
	protected boolean isWeapon;
	protected LinkedList<WeaponProperty> weaponProperties;
	protected LinkedList<WeaponType> weaponType;
	
	public Item(String name) {
		this.name = name;
		enchantmentBonus = 0;
		weaponProperties = new LinkedList<WeaponProperty>();
		weaponType = new LinkedList<WeaponType>();
		isWeapon = false;
	}
	
	public void setMagical(boolean isMagical, int enchantmentBonus) {
		this.isMagical = isMagical;
		this.enchantmentBonus = enchantmentBonus;
	}
	
	public void setSilvered(boolean isSilvered) {
		this.isSilvered = isSilvered;
	}
	
	public LinkedList<WeaponProperty> getWeaponProperties(){
		return weaponProperties;
	}
	
	public LinkedList<WeaponType> getWeaponType(){
		return weaponType;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public DamageDiceGroup getDamageDice() {
		return new DamageDiceGroup(1, 4, DamageType.BLUDGEONING);
	}
	
	public DamageDiceGroup getDamageDiceVersatile() {
		return new DamageDiceGroup(1, 4, DamageType.BLUDGEONING);
	}
	
	
	public boolean isWeapon() {
		return isWeapon;
	}
	
}
