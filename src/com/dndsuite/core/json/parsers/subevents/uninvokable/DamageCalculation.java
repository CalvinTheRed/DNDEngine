package com.dndsuite.core.json.parsers.subevents.uninvokable;

import java.util.ArrayList;

import org.json.simple.JSONObject;

import com.dndsuite.core.Event;
import com.dndsuite.core.GameObject;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.exceptions.SubeventMismatchException;
import com.dndsuite.maths.dice.DamageDiceGroup;

public class DamageCalculation extends Subevent {
	protected ArrayList<DamageDiceGroup> damageDice = new ArrayList<DamageDiceGroup>();

	@Override
	public void parse(JSONObject json, Event e, GameObject eSource, GameObject eTarget)
			throws SubeventMismatchException {
		parent = e;
		String subevent = (String) json.get("subevent");
		addTag(subevent);
		if (!subevent.equals("damage_calculation")) {
			throw new SubeventMismatchException("damage_calculation", subevent);
		}

		// roll();

		presentToEffects(eSource, eTarget);

		eTarget.takeDamage(this);
	}

	@Override
	public DamageCalculation clone() {
		DamageCalculation clone = new DamageCalculation();
		clone.parent = getParentEvent();
		clone.appliedEffects.addAll(appliedEffects);
		clone.tags.addAll(tags);
		clone.damageDice.addAll(damageDice);
		return clone;
	}

	public void addDamageBonus(long bonus, String damageType) {
		for (DamageDiceGroup group : damageDice) {
			if (group.getDamageType().equals(damageType)) {
				group.addBonus(bonus);
				return;
			}
		}
		damageDice.add(new DamageDiceGroup(0, 0, bonus, damageType));
	}

	public void roll() {
		for (DamageDiceGroup group : damageDice) {
			group.roll();
		}
	}

	public ArrayList<DamageDiceGroup> getDamageDice() {
		return damageDice;
	}

	public void setDamageDice(DamageDiceCollection s) {
		damageDice = s.getDamageDice();
	}

	@Override
	public String toString() {
		return "DamageCalculation Subevent";
	}

}
