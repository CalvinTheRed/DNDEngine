package dnd.items.armor;

import java.util.LinkedList;

import dnd.data.ArmorProperty;
import dnd.items.Equippable;
import dnd.items.Item;

public abstract class Armor extends Item implements Equippable {
	int baseArmorClass;
	int maxDex;
	int strReq;
	protected LinkedList<ArmorProperty> armorTags;
	
	public Armor(String name, int value, int baseArmorClass, int maxDex, int strReq) {
		super(name, value);
		this.baseArmorClass = baseArmorClass;
		this.maxDex = maxDex;
		this.strReq = strReq;
		armorTags = new LinkedList<ArmorProperty>();
	}
	
	public int getBaseArmorClass() {
		return baseArmorClass;
	}
	
	public int getMaxDex() {
		return maxDex;
	}

}
