package dnd.events.eventgroups;

import java.util.LinkedList;

import dnd.events.Event;

public class EventGroup {
	protected LinkedList<Event> events;
	protected LinkedList<EventGroup> groups;
	
	public EventGroup() {
		events = new LinkedList<Event>();
		groups = new LinkedList<EventGroup>();
	}
	
	public void addEvent(Event e) {
		events.add(e);
	}
	
	public void addEventGroup(EventGroup group) {
		groups.add(group);
	}
	
	public LinkedList<Event> getEvents(){
		LinkedList<Event> list = new LinkedList<Event>();
		list.addAll(events);
		for (EventGroup group : groups) {
			list.addAll(group.getEvents());
		}
		return list;
	}
	
	public String toString() {
		String val = "[";
		for (int i = 0; i < events.size(); i++) {
			val += events.get(i);
			if (i < events.size() - 1) {
				val += ", ";
			}
		}
		for (EventGroup group : groups) {
			val += ", " + group;
		}
		return val + "]";
	}
	
}
