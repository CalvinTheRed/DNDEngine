package com.dndsuite.core.json.parsers.subevents;

import org.json.simple.JSONObject;

import com.dndsuite.core.gameobjects.GameObject;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.exceptions.SubeventMismatchException;

public class TestSubevent extends Subevent {

	@Override
	public void parse(JSONObject json, GameObject source, GameObject target) throws SubeventMismatchException {
		String subevent = (String) json.get("subevent");
		if (!subevent.equals("test_subevent")) {
			throw new SubeventMismatchException("test_subevent", subevent);
		}
		presentToEffects(source, target);
		System.out.println("Test subevent complete!");
	}

	@Override
	public String toString() {
		return "Test Subevent";
	}

}
