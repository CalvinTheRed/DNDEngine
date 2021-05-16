package com.dndsuite.core.json.parsers.subevents;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.dndsuite.core.Event;
import com.dndsuite.core.GameObject;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.exceptions.JSONFormatException;
import com.dndsuite.exceptions.SubeventMismatchException;
import com.dndsuite.maths.dice.Die;

public class SavingThrow extends Subevent implements Calculation {
	private long base;
	private long set = -1;
	private long bonus = 0;

	@Override
	@SuppressWarnings("unchecked")
	public void parse(JSONObject json, Event e, GameObject eSource, GameObject eTarget)
			throws SubeventMismatchException {
		parent = e;
		String subevent = (String) json.get("subevent");
		addTag(subevent);
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
		DiceCheckCalculation dcc = new DiceCheckCalculation();
		JSONObject dccJson = new JSONObject();
		dccJson.put("subevent", "dice_check_calculation");
		dccJson.put("dc_ability", dcAbility);
		dcc.parse(dccJson, e, eSource, eTarget);
		long dc = dcc.get();

		JSONArray fallout;
		if (get() >= dc) {
			fallout = (JSONArray) json.get("pass");
		} else {
			fallout = (JSONArray) json.get("fail");
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
