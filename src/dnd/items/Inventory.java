package dnd.items;

import java.util.LinkedList;

import dnd.items.armor.Armor;
import dnd.items.belts.Belt;
import dnd.items.boots.Boots;
import dnd.items.cloaks.Cloak;
import dnd.items.necklaces.Necklace;
import dnd.items.rings.Ring;

public class Inventory {
	private LinkedList<Item> contents;
	private Item mainhand;
	private Item offhand;
	private Armor armor;
	private Necklace necklace;
	private Ring ringLeftHand;
	private Ring ringRightHand;
	private Cloak cloak;
	private Belt belt;
	private Boots boots;
	
	public Inventory() {
		contents = new LinkedList<Item>();
	}
	
	public Item mainhand() {
		return mainhand;
	}
	
	public Item offhand() {
		return offhand;
	}
	
	public Armor armor() {
		return armor;
	}
	
	public Necklace necklace() {
		return necklace;
	}
	
	public Ring ringLeftHand() {
		return ringLeftHand;
	}
	
	public Ring ringRightHand() {
		return ringRightHand;
	}
	
	public Cloak cloak() {
		return cloak;
	}
	
	public Belt belt() {
		return belt;
	}
	
	public Boots boots() {
		return boots;
	}
	
	public void equipWeapon(Item item, boolean versatile) {
		if (mainhand != null) {
			contents.add(mainhand);
		}
		mainhand = item;
		if (versatile) {
			if (offhand != null) {
				contents.add(offhand);
			}
			offhand = item;
		}
	}
	
}
