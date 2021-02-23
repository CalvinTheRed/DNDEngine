package dnd.items;

import java.util.LinkedList;

import engine.patterns.Observer;
import engine.patterns.Subject;

/**
 * A class representing the collection of Items which a creature (or possibly a
 * container) controls and owns.
 * 
 * TODO: consider incorporating this class directly in with Entity, rather than
 * having it be its own class
 * 
 * @author calvi
 *
 */
public class Inventory implements Subject {

	private LinkedList<Item> contents;
	private LinkedList<Observer> observers;
	private Item mainhand;
	private Item offhand;
	private Item armor;

	/**
	 * Constructor for class Inventory
	 */
	public Inventory() {
		contents = new LinkedList<Item>();
		observers = new LinkedList<Observer>();
	}

	@Override
	public void addObserver(Observer o) {
		observers.add(o);

	}

	@Override
	public void removeObserver(Observer o) {
		observers.remove(o);

	}

	@Override
	public void updateObservers() {
		for (Observer o : observers) {
			o.update(this);
		}
	}

	/**
	 * This function returns the Item currently assigned to the main hand of this
	 * Inventory
	 * 
	 * @return {@code Item} main hand item
	 */
	public Item mainhand() {
		return mainhand;
	}

	/**
	 * This function returns the Item currently assigned to the off hand of this
	 * Inventory
	 * 
	 * @return {@code Item} off hand item
	 */
	public Item offhand() {
		return offhand;
	}

	/**
	 * This function returns the Item currently assigned to the armor slot of this
	 * Inventory
	 * 
	 * @return {@code Item} armor item
	 */
	public Item armor() {
		return armor;
	}

	/**
	 * This function sets the passed Item as the main hand of this Inventory. If an
	 * Item is already in the main hand, it is moved to the general contents
	 * (analogous to a backpack)
	 * 
	 * @param item ({@code Item}) the Item to be held in the main hand
	 */
	public void equipWeapon(Item item) {
		if (mainhand != null) {
			contents.add(mainhand);
		}
		mainhand = item;
		updateObservers();
	}

}
