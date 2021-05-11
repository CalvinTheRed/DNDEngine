package com.dndsuite.core.json.parsers.subevents;

import org.json.simple.JSONObject;

import com.dndsuite.core.events.Event;
import com.dndsuite.core.gameobjects.GameObject;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.exceptions.SubeventMismatchException;

public class TestSubevent extends Subevent {

	@Override
	public void parse(JSONObject json, Event e, GameObject eventSource, GameObject eventTarget)
			throws SubeventMismatchException {
		String subevent = (String) json.get("subevent");
		addTag(subevent);
		if (!subevent.equals("test_subevent")) {
			throw new SubeventMismatchException("test_subevent", subevent);
		}
		presentToEffects(eventSource, eventTarget);
		System.out.println("Test subevent complete!");
	}

	@Override
	public String toString() {
		return "Test Subevent";
	}

	@Override
	public TestSubevent clone() {
		TestSubevent clone = new TestSubevent();
		return clone;
	}

}
