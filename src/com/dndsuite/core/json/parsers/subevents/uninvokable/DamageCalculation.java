package com.dndsuite.core.json.parsers.subevents.uninvokable;

import java.util.ArrayList;

import org.json.simple.JSONObject;

import com.dndsuite.core.Event;
import com.dndsuite.core.GameObject;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.exceptions.JSONFormatException;
import com.dndsuite.exceptions.SubeventMismatchException;
import com.dndsuite.maths.dice.DamageDiceGroup;

/**
 * DamageCalculation is a derivation of Subevent which cannot be invoked from
 * within a JSON file. This Subevent is responsible for applying bonuses,
 * penalties, and other modifiers to rolled damage dice. The final result is
 * passed as a damage value to the target GameObject.
 * 
 * @author Calvin Withun
 *
 */
public class DamageCalculation extends Subevent {
	protected ArrayList<DamageDiceGroup> damageDice = new ArrayList<DamageDiceGroup>();

	@Override
	public void parse(JSONObject json, Event e, GameObject eSource, GameObject eTarget)
			throws SubeventMismatchException, JSONFormatException {
		super.parse(json, e, eSource, eTarget);
		if (!(json.containsKey("subevent"))) {
			throw new JSONFormatException();
		}
		String subevent = (String) json.get("subevent");
		if (!subevent.equals("damage_calculation")) {
			throw new SubeventMismatchException("damage_calculation", subevent);
		}

		presentToEffects(eSource, eTarget);

		eTarget.takeDamage(this);
	}

	@Override
	public DamageCalculation clone() {
		DamageCalculation clone = new DamageCalculation();
		clone.parent = getParentEvent();
		clone.appliedEffects.addAll(appliedEffects);
		clone.tags.addAll(tags);
		clone.damageDice.addAll(damageDice);
		return clone;
	}

	public void addDamageBonus(long bonus, String damageType) {
		for (DamageDiceGroup group : damageDice) {
			if (group.getDamageType().equals(damageType)) {
				group.addBonus(bonus);
				return;
			}
		}
		damageDice.add(new DamageDiceGroup(0, 0, bonus, damageType));
	}

	/**
	 * This function is intended to provide a mechanism for re-rolling all of the
	 * damage dice, if desired. Note that this will likely impact other damage rolls
	 * generated by the same Event, if any damage is dealt after this Subevent is
	 * over.
	 */
	public void roll() {
		for (DamageDiceGroup group : damageDice) {
			group.roll();
		}
	}

	public ArrayList<DamageDiceGroup> getDamageDice() {
		return damageDice;
	}

	public void setDamageDice(DamageDiceCollection s) {
		damageDice = s.getDamageDice();
	}

	@Override
	public String toString() {
		return "DamageCalculation Subevent";
	}

}
