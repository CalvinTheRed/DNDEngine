package com.dndsuite.core.json.parsers.subevents;

import java.util.ArrayList;

import org.json.simple.JSONObject;

import com.dndsuite.core.events.Event;
import com.dndsuite.core.gameobjects.GameObject;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.exceptions.SubeventMismatchException;
import com.dndsuite.maths.dice.DamageDiceGroup;
import com.dndsuite.maths.dice.Die;

public class DamageCalculation extends Subevent {
	protected ArrayList<DamageDiceGroup> damageDice = new ArrayList<DamageDiceGroup>();;

	@Override
	public void parse(JSONObject json, Event e, GameObject eSource, GameObject eTarget)
			throws SubeventMismatchException {
		parent = e;
		String subevent = (String) json.get("subevent");
		addTag(subevent);
		if (!subevent.equals("damage_calculation")) {
			throw new SubeventMismatchException("damage_calculation", subevent);
		}

		presentToEffects(eSource, eTarget);
		eTarget.takeDamage(this);
	}

	@Override
	public DamageCalculation clone() {
		DamageCalculation clone = new DamageCalculation();
		clone.appliedEffects.addAll(appliedEffects);
		clone.tags.addAll(tags);
		clone.damageDice.addAll(damageDice);
		return clone;
	}

	public void addDamageDiceGroup(DamageDiceGroup newGroup) {
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

	public void addDamage(long bonus, String damageType) {
		for (DamageDiceGroup group : damageDice) {
			if (group.getDamageType().equals(damageType)) {
				group.addBonus(bonus);
				return;
			}
		}
		DamageDiceGroup newGroup = new DamageDiceGroup(0, 0, damageType);
		newGroup.addBonus(bonus);
		damageDice.add(newGroup);
	}

	public void roll() {
		for (DamageDiceGroup group : damageDice) {
			group.roll();
		}
	}

	public ArrayList<DamageDiceGroup> getDamageDice() {
		return damageDice;
	}

	@Override
	public String toString() {
		return "DamageCalculation Subevent";
	}

}
