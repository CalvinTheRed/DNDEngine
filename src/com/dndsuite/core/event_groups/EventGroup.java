package com.dndsuite.core.event_groups;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.dndsuite.core.Event;

/**
 * EventGroup represents a collection of alternative Events. Only one Event may
 * be invoked from this collection, as each entry represents an alternative way
 * of performing the same single Event. Performing multiple Events requires
 * multiple EventGroups.
 * 
 * @author Calvin Withun
 *
 */
public class EventGroup {
	protected ArrayList<Event> events;

	/**
	 * Blank constructor. An EventGroup constructed with this constructor will
	 * contain no Events initially. This constructor is primarily intended to be
	 * used by classes derived from EventGroup.
	 */
	public EventGroup() {
		events = new ArrayList<Event>();
	}

	/**
	 * Standard constructor.
	 * 
	 * @param json - contains a list of Event names to be loaded and included in
	 *             this EventGroup
	 */
	public EventGroup(JSONObject json) {
		events = new ArrayList<Event>();
		JSONArray list = (JSONArray) json.get("events");
		for (Object o : list) {
			String eventName = (String) o;
			events.add(new Event(eventName));
		}
	}

	/**
	 * This function determines whether the EventGroup includes a particular Event.
	 * 
	 * @param e - the particular Event
	 * @return true if e is present, false if e is not present
	 */
	public boolean contains(Event e) {
		return events.contains(e);
	}

	/**
	 * This function is a getter for the contents of the EventGroup.
	 * 
	 * @return the collection of Events in the EventGroup
	 */
	public ArrayList<Event> getEvents() {
		return events;
	}

}
