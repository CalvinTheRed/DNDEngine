package com.dndsuite.core.json.parsers.subevents.invokable;

import org.json.simple.JSONObject;

import com.dndsuite.core.Event;
import com.dndsuite.core.GameObject;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.exceptions.JSONFormatException;
import com.dndsuite.exceptions.SubeventMismatchException;

public class InvokeEvent extends Subevent {

	@Override
	@SuppressWarnings("unchecked")
	public void parse(JSONObject json, Event e, GameObject eSource, GameObject eTarget)
			throws SubeventMismatchException, JSONFormatException {
		parent = e;
		String subevent = (String) json.get("subevent");
		addTag(subevent);
		if (!subevent.equals("invoke_event")) {
			throw new SubeventMismatchException("invoke_event", subevent);
		}

		String eventName = (String) json.get("event");
		Event event = new Event(eventName);

		JSONObject pauseNotes = new JSONObject();
		pauseNotes.put("requirement", "event_invocation");
		pauseNotes.put("source", eSource);
		event.pause(pauseNotes);
	}

	@Override
	public String toString() {
		return "InvokeEvent Subevent";
	}

	@Override
	public InvokeEvent clone() {
		InvokeEvent clone = new InvokeEvent();
		clone.parent = getParentEvent();
		return clone;
	}

}
