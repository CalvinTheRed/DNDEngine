package com.dndsuite.core.events;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.dndsuite.core.gameobjects.GameObject;
import com.dndsuite.core.json.JSONLoader;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.core.json.parsers.subevents.ApplyEffect;
import com.dndsuite.core.json.parsers.subevents.TestSubevent;
import com.dndsuite.dnd.VirtualBoard;
import com.dndsuite.exceptions.SubeventMismatchException;
import com.dndsuite.maths.Vector;

public class Event extends JSONLoader {

	public static final Map<String, Subevent> SUBEVENT_MAP = new HashMap<String, Subevent>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = -5295424280559257214L;

		{
			put("test_subevent", new TestSubevent());
			put("apply_effect", new ApplyEffect());
		}
	};

	public Event(JSONObject json) {
		super(json);
	}

	public Event(String file) {
		super("events/" + file);
	}

	@Override
	protected void parseBasePattern() {
		// TODO Auto-generated method stub

	}

	public void invoke(Vector targetPos, GameObject source) {
		JSONArray subevents = (JSONArray) json.get("subevents");
		for (Object o : subevents) {
			JSONObject subevent = (JSONObject) o;
			try {
				for (GameObject target : VirtualBoard.objectsInAreaOfEffect(source.getPos(), targetPos, json)) {
					SUBEVENT_MAP.get(subevent.get("subevent")).clone().parse(subevent, this, source, target);
				}
			} catch (SubeventMismatchException ex) {
				ex.printStackTrace();
			}
		}
	}

}
