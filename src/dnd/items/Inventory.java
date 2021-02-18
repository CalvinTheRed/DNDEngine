package dnd.items;

import java.util.LinkedList;

import engine.patterns.Observer;
import engine.patterns.Subject;

public class Inventory implements Subject {
	
	private LinkedList<Item>     contents;
	private LinkedList<Observer> observers;
	private Item mainhand;
	private Item offhand;
	private Item armor;
	
	public Inventory() {
		contents  = new LinkedList<Item>();
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
	
	public Item mainhand() {
		return mainhand;
	}
	
	public Item offhand() {
		return offhand;
	}
	
	public Item armor() {
		return armor;
	}
	
	public void equipWeapon(Item item) {
		if (mainhand != null) {
			contents.add(mainhand);
		}
		mainhand = item;
		updateObservers();
	}
	
}
