package com.dndsuite.core.json.parsers.subevents.uninvokable;

import java.util.ArrayList;

import org.json.simple.JSONObject;

import com.dndsuite.core.Event;
import com.dndsuite.core.GameObject;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.exceptions.SubeventMismatchException;
import com.dndsuite.maths.dice.DamageDiceGroup;
import com.dndsuite.maths.dice.Die;

/**
 * DamageDiceCollection is a derivation of Subevent which cannot be invoked from
 * within a JSON file. This Subevent is responsible for collecting all dice
 * involved in a damage roll, prior to applying any modifiers to those dice. All
 * dice are rolled upon being added to this Subevent.
 * 
 * @author Calvin Withun
 *
 */
public class DamageDiceCollection extends Subevent {
	protected ArrayList<DamageDiceGroup> damageDice = new ArrayList<DamageDiceGroup>();

	@Override
	@SuppressWarnings("unchecked")
	public void parse(JSONObject json, Event e, GameObject eSource, GameObject eTarget)
			throws SubeventMismatchException {
		parent = e;
		String subevent = (String) json.get("subevent");
		addTag(subevent);
		if (!subevent.equals("damage_dice_collection")) {
			throw new SubeventMismatchException("damage_dice_collection", subevent);
		}

		presentToEffects(eSource, eTarget);

		JSONObject sJson = new JSONObject();
		sJson.put("subevent", "damage_calculation");
		DamageCalculation s = new DamageCalculation();
		s.setDamageDice(this);
		s.parse(sJson, e, eSource, eTarget);
	}

	@Override
	public DamageDiceCollection clone() {
		DamageDiceCollection clone = new DamageDiceCollection();
		clone.parent = getParentEvent();
		clone.appliedEffects.addAll(appliedEffects);
		clone.tags.addAll(tags);
		clone.damageDice.addAll(damageDice);
		return clone;
	}

	public void addDamageDiceGroup(DamageDiceGroup newGroup) {
		newGroup.roll();
		for (DamageDiceGroup group : damageDice) {
			if (group.getDamageType().equals(newGroup.getDamageType())) {
				for (Die die : newGroup.getDice()) {
					group.addDie(die);
				}
				group.addBonus(newGroup.getBonus());
				return;
			}
		}
		damageDice.add(newGroup);
	}

	public ArrayList<DamageDiceGroup> getDamageDice() {
		return damageDice;
	}

	@Override
	public String toString() {
		return "DamageDiceCollection Subevent";
	}

}
