package com.dndsuite.core.json.parsers.subevents;

import org.json.simple.JSONObject;

import com.dndsuite.core.events.Event;
import com.dndsuite.core.gameobjects.GameObject;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.exceptions.SubeventMismatchException;

public class AbilityScoreCalculation extends Subevent implements Calculation {
	private int base;
	private int set = -1;
	private int bonus = 0;

	@Override
	public void parse(JSONObject json, Event e, GameObject eSource, GameObject eTarget)
			throws SubeventMismatchException {
		String subevent = (String) json.get("subevent");
		addTag(subevent);
		if (!subevent.equals("ability_score_calculation")) {
			throw new SubeventMismatchException("ability_score_calculation", subevent);
		}

		String ability = (String) json.get("ability");
		JSONObject abilityScores = (JSONObject) eTarget.getJSONData().get("ability_scores");
		base = (int) abilityScores.get(ability);

		presentToEffects(eSource, eTarget);
	}

	@Override
	public String toString() {
		return "AbilityScoreCalculation Subevent";
	}

	@Override
	public AbilityScoreCalculation clone() {
		AbilityScoreCalculation clone = new AbilityScoreCalculation();
		return clone;
	}

	@Override
	public void setTo(int set) {
		if (set > this.set) {
			this.set = set;
		}
	}

	@Override
	public void addBonus(int bonus) {
		this.bonus += bonus;
	}

	@Override
	public int get() {
		if (set != -1) {
			return set;
		}
		return base + bonus;
	}

}
