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
	
	public boolean stowMainhand() {
		if (mainhand == null) {
			return false;
		}
		contents.add(mainhand);
		mainhand = null;
		return true;
	}
	
	public boolean stowOffhand() {
		if (offhand == null) {
			return false;
		}
		contents.add(offhand);
		offhand = null;
		return true;
	}
	
	public boolean doffArmor() {
		if (armor == null) {
			return false;
		}
		contents.add(armor);
		armor = null;
		return true;
	}
	
	public boolean doffNecklace() {
		if (necklace == null) {
			return false;
		}
		contents.add(necklace);
		necklace = null;
		return true;
	}
	
	public boolean doffRingLeftHand() {
		if (ringLeftHand == null) {
			return false;
		}
		contents.add(ringLeftHand);
		ringLeftHand = null;
		return true;
	}
	
	public boolean doffRingRightHand() {
		if (ringRightHand == null) {
			return false;
		}
		contents.add(ringRightHand);
		ringRightHand = null;
		return true;
	}
	
	public boolean doffCloak() {
		if (cloak == null) {
			return false;
		}
		contents.add(cloak);
		cloak = null;
		return true;
	}
	
	public boolean doffBelt() {
		if (belt == null) {
			return false;
		}
		contents.add(belt);
		belt = null;
		return true;
	}
	
	public boolean doffBoots() {
		if (belt == null) {
			return false;
		}
		contents.add(boots);
		boots = null;
		return true;
	}
	
	public boolean dropMainhand() {
		if (mainhand == null) {
			return false;
		}
		// TODO: drop Item into world
		mainhand = null;
		return true;
	}
	
	public boolean dropOffhand() {
		if (offhand == null) {
			return false;
		}
		// TODO: drop Item into world
		offhand = null;
		return true;
	}
	
	public boolean dropArmor() {
		if (armor == null) {
			return false;
		}
		// TODO: drop Item into world
		armor = null;
		return true;
	}
	
	public boolean dropNecklace() {
		if (necklace == null) {
			return false;
		}
		// TODO: drop Item into world
		necklace = null;
		return true;
	}
	
	public boolean dropRingLeftHand() {
		if (ringLeftHand == null) {
			return false;
		}
		// TODO: drop Item into world
		ringLeftHand = null;
		return true;
	}
	
	public boolean dropRingRightHand() {
		if (ringRightHand == null) {
			return false;
		}
		// TODO: drop Item into world
		ringRightHand = null;
		return true;
	}
	
	public boolean dropCloak() {
		if (cloak == null) {
			return false;
		}
		// TODO: drop Item into world
		cloak = null;
		return true;
	}
	
	public boolean dropBelt() {
		if (belt == null) {
			return false;
		}
		// TODO: drop Item into world
		belt = null;
		return true;
	}
	
	public boolean dropBoots() {
		if (boots == null) {
			return false;
		}
		// TODO: drop Item into world
		boots = null;
		return true;
	}
	
	public boolean dropItem(Item item) {
		if (contents.contains(item)) {
			// drop Item into world
			contents.remove(item);
			return true;
		}
		return false;
	}
	
}
