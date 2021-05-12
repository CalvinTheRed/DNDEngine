package com.dndsuite.core.json.parsers.subevents;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.dndsuite.core.events.Event;
import com.dndsuite.core.gameobjects.GameObject;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.exceptions.SubeventMismatchException;
import com.dndsuite.maths.dice.Die;

public class AttackRoll extends Subevent implements Calculation {
	private int base;
	private int set = -1;
	private int bonus = 0;
	private int criticalThreshold;

	@Override
	@SuppressWarnings("unchecked")
	public void parse(JSONObject json, Event e, GameObject eSource, GameObject eTarget)
			throws SubeventMismatchException {
		String subevent = (String) json.get("subevent");
		addTag(subevent);
		if (!subevent.equals("attack_roll")) {
			throw new SubeventMismatchException("attack_roll", subevent);
		}

		// Load critical hit threshold (default is 20)
		if (json.containsKey("critical_threshold")) {
			criticalThreshold = (int) json.get("critical_threshold");
		} else {
			criticalThreshold = 20;
		}

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
			int tmp = d20.getRoll();
			d20.roll();
			if (d20.getRoll() > tmp) {
				base = d20.getRoll();
			} else {
				base = tmp;
			}
		} else if (hasTag("disadvantage")) {
			int tmp = d20.getRoll();
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
		int ac = acc.get();

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
		return clone;
	}

	public void setCriticalThreshold(int criticalThreshold) {
		if (criticalThreshold < this.criticalThreshold) {
			this.criticalThreshold = criticalThreshold;
		}
	}

	public int getCriticalThreshold() {
		return criticalThreshold;
	}

	@Override
	public void addBonus(int bonus) {
		this.bonus += bonus;
	}

	@Override
	public void setTo(int set) {
		this.set = set;
	}

	@Override
	public int get() {
		if (set != -1) {
			return set + bonus;
		}
		return base + bonus;
	}

}
