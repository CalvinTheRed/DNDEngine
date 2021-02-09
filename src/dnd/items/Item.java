package dnd.items;

import java.util.LinkedList;

import dnd.events.Event;

public abstract class Item {
	protected String name;
	
	public Item(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public LinkedList<Event> getAttackOptions() {
		LinkedList<Event> events = new LinkedList<Event>();
		
		return events;
	}
	
}
