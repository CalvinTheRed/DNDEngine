package com.dndsuite.core.json.parsers.subevents.uninvokable;

import org.json.simple.JSONObject;

import com.dndsuite.core.Event;
import com.dndsuite.core.GameObject;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.core.json.parsers.subevents.Calculation;
import com.dndsuite.exceptions.JSONFormatException;
import com.dndsuite.exceptions.SubeventMismatchException;

/**
 * AbilityScoreCalculation is a derivation of Subevent which cannot be invoked
 * from within a JSON file. This Subevent is responsible for determining the
 * effective value of a GameObject's ability score. This may be different from
 * or the same as that ability score's base value.
 * 
 * @author Calvin Withun
 *
 */
public class AbilityScoreCalculation extends Subevent implements Calculation {
	private long base;
	private long set = -1;
	private long bonus = 0;

	@Override
	public void parse(JSONObject json, Event e, GameObject eSource, GameObject eTarget)
			throws SubeventMismatchException, JSONFormatException {
		super.parse(json, e, eSource, eTarget);
		if (!(json.containsKey("subevent") && json.containsKey("ability"))) {
			throw new JSONFormatException();
		}
		String subevent = (String) json.get("subevent");
		addTag("calculation");
		if (!subevent.equals("ability_score_calculation")) {
			throw new SubeventMismatchException("ability_score_calculation", subevent);
		}

		String ability = (String) json.get("ability");
		JSONObject abilityScores = (JSONObject) eTarget.getJSONData().get("ability_scores");
		base = (long) abilityScores.get(ability);

		presentToEffects(eSource, eTarget);
	}

	@Override
	public String toString() {
		return "AbilityScoreCalculation Subevent";
	}

	@Override
	public AbilityScoreCalculation clone() {
		AbilityScoreCalculation clone = new AbilityScoreCalculation();
		clone.parent = getParentEvent();
		clone.base = base;
		clone.set = set;
		clone.bonus = bonus;
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

	@Override
	public long getRaw() {
		if (set != -1) {
			return set;
		}
		return base;
	}

}
