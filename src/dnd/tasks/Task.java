package dnd.tasks;

import java.util.LinkedList;

import dnd.events.EventGroup;
import gameobjects.entities.Entity;

public abstract class Task {
	private String name;
	private LinkedList<EventGroup> eventGroups;
	//TODO: action economy cost
	
	public Task(String name) {
		this.name = name;
		eventGroups = new LinkedList<EventGroup>();
	}
	
	protected void addEventGroup(EventGroup group) {
		eventGroups.add(group);
	}
	
	public boolean invoke(Entity invoker) {
		// TODO: return false if insufficient action economy
		// TODO: expend action economy
		for (EventGroup group : eventGroups) {
			invoker.addToEventQueue(group);
		}
		return true;
	}
	
	public String toString() {
		return name;
	}
}
