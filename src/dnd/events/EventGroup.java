package dnd.events;

import java.util.LinkedList;

public class EventGroup {
	private LinkedList<Event> events;
	
	public EventGroup() {
		events = new LinkedList<Event>();
	}
	
	public void addEvent(Event e) {
		events.add(e);
	}
	
	public LinkedList<Event> getEvents(){
		return events;
	}
	
	public String toString() {
		String val = "[";
		for (int i = 0; i < events.size(); i++) {
			val += events.get(i);
			if (i < events.size() - 1) {
				val += ", ";
			}
		}
		return val + "]";
	}
	
}
