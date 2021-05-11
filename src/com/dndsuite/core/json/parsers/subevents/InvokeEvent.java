package com.dndsuite.core.json.parsers.subevents;

import org.json.simple.JSONObject;

import com.dndsuite.core.events.Event;
import com.dndsuite.core.gameobjects.GameObject;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.exceptions.SubeventMismatchException;
import com.dndsuite.maths.Vector;

public class InvokeEvent extends Subevent {

	@Override
	public void parse(JSONObject json, Event e, GameObject eSource, GameObject eTarget)
			throws SubeventMismatchException {
		String subevent = (String) json.get("subevent");
		addTag(subevent);
		if (!subevent.equals("invoke_event")) {
			throw new SubeventMismatchException("invoke_event", subevent);
		}

		String eventName = (String) json.get("event");
		Event event = new Event(eventName);

		Vector targetPos = null;
		String targetPosAlias = (String) json.get("target_pos");
		if (targetPosAlias.equals("source")) {
			targetPos = eSource.getPos();
		} else if (targetPosAlias.equals("target")) {
			targetPos = eTarget.getPos();
		} else if (targetPosAlias.equals("custom")) {
			// TODO: implement this
			// Some kind of interrupt requiring outside input?
		}
		event.invoke(targetPos, eSource);
	}

	@Override
	public String toString() {
		return "InvokeEvent Subevent";
	}

	@Override
	public InvokeEvent clone() {
		InvokeEvent clone = new InvokeEvent();
		return clone;
	}

}
