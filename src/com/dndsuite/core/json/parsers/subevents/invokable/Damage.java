package com.dndsuite.core.json.parsers.subevents.invokable;

import org.json.simple.JSONObject;

import com.dndsuite.core.Event;
import com.dndsuite.core.GameObject;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.core.json.parsers.subevents.uninvokable.DamageDiceCollection;
import com.dndsuite.exceptions.JSONFormatException;
import com.dndsuite.exceptions.SubeventMismatchException;

/**
 * Damage is a derivation of Subevent which can be invoked from within a JSON
 * file. This Subevent generates and parses a DamageDiceCollection Subevent to
 * begin the process of determining damage. Damage is typically determined by
 * the JSON data of the parent Event.
 * 
 * @author Calvin Withun
 *
 */
public class Damage extends Subevent {

	@SuppressWarnings("unchecked")
	@Override
	public void parse(JSONObject json, Event e, GameObject eSource, GameObject eTarget)
			throws SubeventMismatchException, JSONFormatException {
		super.parse(json, e, eSource, eTarget);
		String subevent = (String) json.get("subevent");
		if (!subevent.equals("damage")) {
			throw new SubeventMismatchException("damage", subevent);
		}

		DamageDiceCollection s = e.getBaseDiceCollection();
		JSONObject sJson = new JSONObject();
		sJson.put("subevent", "damage_dice_collection");
		s.parse(sJson, e, eSource, eTarget);
	}

	@Override
	public Damage clone() {
		Damage clone = new Damage();
		clone.parent = getParentEvent();
		return clone;
	}

}
