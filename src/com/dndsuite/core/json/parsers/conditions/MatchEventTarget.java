package com.dndsuite.core.json.parsers.conditions;

import org.json.simple.JSONObject;

import com.dndsuite.core.Effect;
import com.dndsuite.core.json.parsers.Condition;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.exceptions.ConditionMismatchException;
import com.dndsuite.exceptions.JSONFormatException;

/**
 * HasTag is a derivation of Condition. This Condition serves to determine
 * whether a Subevent contains a particular tag.
 * 
 * @author Calvin Withun
 *
 */
public class MatchEventTarget implements Condition {

	@Override
	public boolean parse(JSONObject json, Effect e, Subevent s) throws ConditionMismatchException, JSONFormatException {
		if (!(json.containsKey("condition") && json.containsKey("event_target"))) {
			throw new JSONFormatException();
		}
		String condition = (String) json.get("condition");
		if (!condition.equals("match_event_target")) {
			throw new ConditionMismatchException("match_event_target", condition);
		}

		String eventSourceAlias = (String) json.get("event_target");
		if (eventSourceAlias.equals("effect_source")) {
			return s.getTarget() == e.getSource();
		} else if (eventSourceAlias.equals("effect_target")) {
			return s.getTarget() == e.getTarget();
		} else {
			throw new JSONFormatException();
		}

	}

}
