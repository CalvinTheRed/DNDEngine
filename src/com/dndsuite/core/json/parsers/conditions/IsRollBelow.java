package com.dndsuite.core.json.parsers.conditions;

import org.json.simple.JSONObject;

import com.dndsuite.core.Effect;
import com.dndsuite.core.json.parsers.Condition;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.core.json.parsers.subevents.Calculation;
import com.dndsuite.exceptions.ConditionMismatchException;

/**
 * IsRollBelow is a derivation of Condition. THis Condition serves to determine
 * whether a d20 roll falls below a particular threshold. This Condition returns
 * false if it is provided any Subevent which does not entail the rolling of a
 * 20-sided Die.
 * 
 * @author Calvin Withun
 *
 */
public class IsRollBelow implements Condition {

	@Override
	public boolean parse(JSONObject json, Effect e, Subevent s) throws ConditionMismatchException {
		String condition = (String) json.get("condition");
		if (!condition.equals("is_roll_below")) {
			throw new ConditionMismatchException("is_roll_below", condition);
		}

		// TODO: this would allow for ArmorClassCalculations and
		// DiceCheckCalculations...
		if (s.hasTag("calculation")) {
			Calculation c = (Calculation) s;
			return c.getRaw() < (long) json.get("value");
		}

		return false;
	}

}
