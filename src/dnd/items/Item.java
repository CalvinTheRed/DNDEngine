package dnd.items;

import java.util.LinkedList;

import dnd.data.WeaponProperty;
import dnd.data.WeaponType;

public abstract class Item {
	protected String name;
	protected int value;
	protected int enchantmentBonus;
	protected boolean isMagical;
	protected boolean isSilvered;
	protected LinkedList<WeaponProperty> weaponProperties;
	protected LinkedList<WeaponType> weaponType;
	
	public Item(String name, int value) {
		this.name = name;
		this.value = value;
		enchantmentBonus = 0;
		weaponProperties = new LinkedList<WeaponProperty>();
		weaponType = new LinkedList<WeaponType>();
	}
	
	public void setMagical(boolean isMagical, int enchantmentBonus) {
		this.isMagical = isMagical;
		this.enchantmentBonus = enchantmentBonus;
	}
	
	public void setSilvered(boolean isSilvered) {
		this.isSilvered = isSilvered;
	}
	
	public int getValue() {
		return value;
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
	
}
