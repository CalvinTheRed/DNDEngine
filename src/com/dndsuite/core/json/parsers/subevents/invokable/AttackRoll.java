package com.dndsuite.core.json.parsers.subevents.invokable;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.dndsuite.core.Event;
import com.dndsuite.core.GameObject;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.core.json.parsers.subevents.Calculation;
import com.dndsuite.core.json.parsers.subevents.uninvokable.ArmorClassCalculation;
import com.dndsuite.exceptions.JSONFormatException;
import com.dndsuite.exceptions.SubeventMismatchException;
import com.dndsuite.maths.dice.Die;

/**
 * AttackRoll is a derivation of Subevent which can be invoked from within a
 * JSON file. This Subevent rolls a d20 (which can have bonuses applied to it or
 * be set to a given value) against the results of an ArmorClassCalculation
 * Subevent. Different results may happen according to whether the roll value
 * meets or exceeds the target threshold.
 * 
 * @author Calvin Withun
 *
 */
public class AttackRoll extends Subevent implements Calculation {
	private long base;
	private long set = -1;
	private long bonus = 0;
	private long criticalThreshold;

	@Override
	@SuppressWarnings("unchecked")
	public void parse(JSONObject json, Event e, GameObject eSource, GameObject eTarget)
			throws SubeventMismatchException, JSONFormatException {
		super.parse(json, e, eSource, eTarget);
		if (!(json.containsKey("subevent") && json.containsKey("attack_ability"))) {
			throw new JSONFormatException();
		}
		String subevent = (String) json.get("subevent");
		addTag("calculation");
		if (!subevent.equals("attack_roll")) {
			throw new SubeventMismatchException("attack_roll", subevent);
		}

		criticalThreshold = (long) json.getOrDefault("critical_threshold", 20L);

		// Load attack ability
		String attackAbility = (String) json.get("attack_ability");
		addTag(attackAbility);

		presentToEffects(eSource, eTarget);

		// Roll d20 & account for advantage and disadvantage
		Die d20 = new Die(20);
		d20.roll();
		if (hasTag("advantage") & hasTag("disadvantage")) {
			base = d20.getRoll();
		} else if (hasTag("advantage")) {
			long tmp = d20.getRoll();
			d20.roll();
			if (d20.getRoll() > tmp) {
				base = d20.getRoll();
			} else {
				base = tmp;
			}
		} else if (hasTag("disadvantage")) {
			long tmp = d20.getRoll();
			d20.roll();
			if (d20.getRoll() < tmp) {
				base = d20.getRoll();
			} else {
				base = tmp;
			}
		} else {
			base = d20.getRoll();
		}

		// Check for critical hit/miss and inform parent Event
		if (base >= criticalThreshold) {
			e.addTag("critical_hit");
		} else if (base == 1) {
			e.addTag("critical_miss");
		}

		// Apply attack ability bonus
		addBonus(eSource.getAbilityModifier(attackAbility));

		// Determine the armor class of the target
		ArmorClassCalculation armorClassCalculation = new ArmorClassCalculation();
		JSONObject subeventJson = new JSONObject();
		subeventJson.put("subevent", "armor_class_calculation");
		armorClassCalculation.parse(subeventJson, e, eSource, eTarget);
		long ac = armorClassCalculation.get();

		JSONArray fallout;
		if ((get() >= ac && !e.hasTag("critical_miss")) || base >= criticalThreshold) {
			fallout = (JSONArray) json.get("hit");
		} else {
			fallout = (JSONArray) json.get("miss");
		}

		for (Object o : fallout) {
			JSONObject falloutSubevent = (JSONObject) o;
			try {
				Event.SUBEVENT_MAP.get(falloutSubevent.get("subevent")).clone().parse(falloutSubevent, e, eSource,
						eTarget);
			} catch (SubeventMismatchException ex) {
				ex.printStackTrace();
			} catch (JSONFormatException ex) {
				ex.printStackTrace();
			}
		}
	}

	@Override
	public String toString() {
		return "AttackRoll Subevent";
	}

	@Override
	public AttackRoll clone() {
		AttackRoll clone = new AttackRoll();
		clone.parent = getParentEvent();
		clone.base = base;
		clone.set = set;
		clone.bonus = bonus;
		return clone;
	}

	/**
	 * This function modifies the value which must be met or exceeded by a d20 roll
	 * (with no bonuses) in order for a critical hit to occur.
	 * 
	 * @param criticalThreshold - the new critical hit threshold
	 */
	public void setCriticalThreshold(int criticalThreshold) {
		if (criticalThreshold < this.criticalThreshold) {
			this.criticalThreshold = criticalThreshold;
		}
	}

	/**
	 * This function returns the value which must be met or exceeded by a d20 roll
	 * (with no bonuses) in order for a critical hit to occur.
	 * 
	 * @return the critical hit threshold
	 */
	public long getCriticalThreshold() {
		return criticalThreshold;
	}

	@Override
	public void addBonus(long bonus) {
		this.bonus += bonus;
	}

	@Override
	public void setTo(long set) {
		this.set = set;
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
