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

public class AttackRoll extends Subevent implements Calculation {
	private long base;
	private long set = -1;
	private long bonus = 0;
	private long criticalThreshold;

	@Override
	@SuppressWarnings("unchecked")
	public void parse(JSONObject json, Event e, GameObject eSource, GameObject eTarget)
			throws SubeventMismatchException {
		parent = e;
		String subevent = (String) json.get("subevent");
		addTag(subevent);
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
		ArmorClassCalculation acc = new ArmorClassCalculation();
		JSONObject accJson = new JSONObject();
		accJson.put("subevent", "armor_class_calculation");
		acc.parse(accJson, e, eSource, eTarget);
		long ac = acc.get();

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
		return clone;
	}

	public void setCriticalThreshold(int criticalThreshold) {
		if (criticalThreshold < this.criticalThreshold) {
			this.criticalThreshold = criticalThreshold;
		}
	}

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
