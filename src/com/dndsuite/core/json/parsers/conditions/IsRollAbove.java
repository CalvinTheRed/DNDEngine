package com.dndsuite.core.json.parsers.conditions;

import org.json.simple.JSONObject;

import com.dndsuite.core.effects.Effect;
import com.dndsuite.core.json.parsers.Condition;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.core.json.parsers.subevents.AttackRoll;
import com.dndsuite.exceptions.ConditionMismatchException;

public class IsRollAbove implements Condition {

	@Override
	public boolean parse(JSONObject json, Effect e, Subevent s) throws ConditionMismatchException {
		String condition = (String) json.get("condition");
		if (!condition.equals("is_roll_above")) {
			throw new ConditionMismatchException("is_roll_above", condition);
		}

		if (s.hasTag("attack_roll")) {
			AttackRoll ar = (AttackRoll) s;
			return ar.getRaw() > (long) json.get("value");
		}

		return false;
	}

}
