package com.dndsuite.core.json.parsers.subevents;

import org.json.simple.JSONObject;

import com.dndsuite.core.events.Event;
import com.dndsuite.core.gameobjects.GameObject;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.exceptions.SubeventMismatchException;

public class Damage extends Subevent {

	@SuppressWarnings("unchecked")
	@Override
	public void parse(JSONObject json, Event e, GameObject eSource, GameObject eTarget)
			throws SubeventMismatchException {
		parent = e;
		String subevent = (String) json.get("subevent");
		addTag(subevent);
		if (!subevent.equals("damage")) {
			throw new SubeventMismatchException("damage", subevent);
		}

		DamageCalculation damage = e.getBaseDamage();
		JSONObject dJson = new JSONObject();
		dJson.put("subevent", "damage_calculation");
		damage.parse(dJson, e, eSource, eTarget);
	}

	@Override
	public Damage clone() {
		Damage clone = new Damage();
		clone.parent = getParentEvent();
		return clone;
	}

}
