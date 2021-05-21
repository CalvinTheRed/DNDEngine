package com.dndsuite.core.json.parsers.conditions;

import java.util.ArrayList;

import org.json.simple.JSONObject;

import com.dndsuite.core.Effect;
import com.dndsuite.core.Event;
import com.dndsuite.core.json.parsers.Condition;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.exceptions.ConditionMismatchException;
import com.dndsuite.maths.dice.DamageDiceGroup;

/**
 * DealsDamageType is a derivation of Condition. This Condition serves to
 * determine whether an Event deals damage of a particular type. This is
 * determined by iterating through the damage dice provided by the
 * getBaseDiceCollection() function of the Subevent's parent Event.
 * 
 * @author Calvin Withun
 *
 */
public class DealsDamageType implements Condition {

	@Override
	public boolean parse(JSONObject json, Effect e, Subevent s) throws ConditionMismatchException {
		String condition = (String) json.get("condition");
		if (!condition.equals("deals_damage_type")) {
			throw new ConditionMismatchException("deals_damage_type", condition);
		}

		Event parent = s.getParentEvent();
		ArrayList<DamageDiceGroup> damageDice = parent.getBaseDiceCollection().getDamageDice();
		for (DamageDiceGroup group : damageDice) {
			if (group.getDamageType().equals(json.get("damage_type"))) {
				return true;
			}
		}

		return false;
	}

}
