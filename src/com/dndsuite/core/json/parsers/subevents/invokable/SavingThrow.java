package com.dndsuite.core.json.parsers.subevents.invokable;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.dndsuite.core.Event;
import com.dndsuite.core.GameObject;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.core.json.parsers.subevents.Calculation;
import com.dndsuite.core.json.parsers.subevents.uninvokable.DiceCheckCalculation;
import com.dndsuite.exceptions.JSONFormatException;
import com.dndsuite.exceptions.SubeventMismatchException;
import com.dndsuite.maths.dice.Die;

/**
 * SavingThrow is a derivation of Subevent which can be invoked from within a
 * JSON file. This Subevent rolls a d20 (which can have bonuses applied to it or
 * be set to a given value) against the results of a DiceCheckCollection
 * Subevent. Different results may happen according to whether the roll value
 * meets or exceeds the target threshold.
 * 
 * @author Calvin Withun
 *
 */
public class SavingThrow extends Subevent implements Calculation {
	private long base;
	private long set = -1;
	private long bonus = 0;

	@Override
	@SuppressWarnings("unchecked")
	public void parse(JSONObject json, Event e, GameObject eSource, GameObject eTarget)
			throws SubeventMismatchException, JSONFormatException {
		super.parse(json, e, eSource, eTarget);
		if (!(json.containsKey("subevent") && json.containsKey("dc_ability") && json.containsKey("save_ability"))) {

		}
		String subevent = (String) json.get("subevent");
		addTag("calculation");
		if (!subevent.equals("saving_throw")) {
			throw new SubeventMismatchException("saving_throw", subevent);
		}

		// Load dc ability
		String dcAbility = (String) json.get("dc_ability");

		// Load save ability
		String saveAbility = (String) json.get("save_ability");
		addTag(saveAbility);

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

		// Apply save ability bonus
		addBonus(eTarget.getAbilityModifier(saveAbility));

		// Determine the dc of the source
		// TODO: allow for "spell" dc_ability parameter
		DiceCheckCalculation diceCheckCalculation = new DiceCheckCalculation();
		JSONObject subeventJson = new JSONObject();
		subeventJson.put("subevent", "dice_check_calculation");
		subeventJson.put("dc_ability", dcAbility);
		diceCheckCalculation.parse(subeventJson, e, eSource, eTarget);
		long dc = diceCheckCalculation.get();

		JSONArray fallout;
		if (get() >= dc) {
			fallout = (JSONArray) json.getOrDefault("pass", new JSONArray());
		} else {
			fallout = (JSONArray) json.getOrDefault("fail", new JSONArray());
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
		return "SavingThrow Subevent";
	}

	@Override
	public SavingThrow clone() {
		SavingThrow clone = new SavingThrow();
		clone.parent = getParentEvent();
		clone.base = base;
		clone.set = set;
		clone.bonus = bonus;
		return clone;
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
