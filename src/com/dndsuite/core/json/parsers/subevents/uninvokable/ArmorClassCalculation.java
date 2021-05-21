package com.dndsuite.core.json.parsers.subevents.uninvokable;

import org.json.simple.JSONObject;

import com.dndsuite.core.Event;
import com.dndsuite.core.GameObject;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.core.json.parsers.subevents.Calculation;
import com.dndsuite.exceptions.SubeventMismatchException;

/**
 * ArmorClassCalculation is a derivation of Subevent which cannot be invoked
 * from within a JSON file. This Subevent is responsible for determining the
 * armor class of a GameObject.
 * 
 * @author Calvin Withun
 *
 */
public class ArmorClassCalculation extends Subevent implements Calculation {
	private long base;
	private long set = -1;
	private long bonus = 0;

	@Override
	public void parse(JSONObject json, Event e, GameObject eSource, GameObject eTarget)
			throws SubeventMismatchException {
		parent = e;
		String subevent = (String) json.get("subevent");
		addTag(subevent);
		addTag("calculation");
		if (!subevent.equals("armor_class_calculation")) {
			throw new SubeventMismatchException("armor_class_calculation", subevent);
		}

		// TODO: create a better AC base algorithm
		// remember to account for inanimate objects
		base = 10L + eTarget.getAbilityModifier("dex");

		presentToEffects(eSource, eTarget);
	}

	@Override
	public String toString() {
		return "ArmorClassCalculation Subevent";
	}

	@Override
	public ArmorClassCalculation clone() {
		ArmorClassCalculation clone = new ArmorClassCalculation();
		clone.parent = getParentEvent();
		clone.base = base;
		clone.set = set;
		clone.bonus = bonus;
		return clone;
	}

	@Override
	public void setTo(long set) {
		// this function to be used for unarmored defense
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
			return set + bonus;
		}
		return base + bonus;
	}

	@Override
	public long getRaw() {
		if (set != -1) {
			return set;
		}
		return base;
	}

}
