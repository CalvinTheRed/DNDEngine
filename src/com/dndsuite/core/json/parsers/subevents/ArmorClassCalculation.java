package com.dndsuite.core.json.parsers.subevents;

import org.json.simple.JSONObject;

import com.dndsuite.core.events.Event;
import com.dndsuite.core.gameobjects.GameObject;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.exceptions.SubeventMismatchException;

public class ArmorClassCalculation extends Subevent implements Calculation {
	private long base;
	private long set = -1;
	private long bonus = 0;

	@Override
	public void parse(JSONObject json, Event e, GameObject eSource, GameObject eTarget)
			throws SubeventMismatchException {
		String subevent = (String) json.get("subevent");
		addTag(subevent);
		if (!subevent.equals("armor_class_calculation")) {
			throw new SubeventMismatchException("armor_class_calculation", subevent);
		}

		// TODO: create a better AC base algorithm
		base = 10 + eTarget.getAbilityModifier("dex");

		presentToEffects(eSource, eTarget);
	}

	@Override
	public String toString() {
		return "ArmorClassCalculation Subevent";
	}

	@Override
	public ArmorClassCalculation clone() {
		ArmorClassCalculation clone = new ArmorClassCalculation();
		return clone;
	}

	@Override
	public void setTo(long set) {
		if (set > this.set) {
			this.set = set;
		}
	}

	@Override
	public void addBonus(long bonus) {
		this.bonus += bonus;
	}

	@Override
	public long get() {
		if (set != -1) {
			return set;
		}
		return base + bonus;
	}

}
