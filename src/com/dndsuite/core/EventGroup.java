package com.dndsuite.core;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class EventGroup {
	protected ArrayList<Event> events;

	public EventGroup(JSONObject json) {
		JSONArray list = (JSONArray) json.get("events");
		for (Object o : list) {
			String eventName = (String) o;
			events.add(new Event(eventName));
		}
	}

	public boolean contains(Event e) {
		return events.contains(e);
	}

}
