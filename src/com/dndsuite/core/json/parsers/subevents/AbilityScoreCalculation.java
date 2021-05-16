package com.dndsuite.core.json.parsers.subevents;

import org.json.simple.JSONObject;

import com.dndsuite.core.Event;
import com.dndsuite.core.GameObject;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.exceptions.SubeventMismatchException;

public class AbilityScoreCalculation extends Subevent implements Calculation {
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
