package com.dndsuite.core.event_groups;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.dndsuite.core.Event;

public class EventGroup {
	protected ArrayList<Event> events;

	public EventGroup() {
		events = new ArrayList<Event>();
	}

	public EventGroup(JSONObject json) {
		events = new ArrayList<Event>();
		JSONArray list = (JSONArray) json.get("events");
		for (Object o : list) {
			String eventName = (String) o;
			events.add(new Event(eventName));
		}
	}

	public boolean contains(Event e) {
		return events.contains(e);
	}

	public ArrayList<Event> getEvents() {
		return events;
	}

}
