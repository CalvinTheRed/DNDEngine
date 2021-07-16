package com.dndsuite.core.json.parsers.conditions;

import org.json.simple.JSONObject;

import com.dndsuite.core.Effect;
import com.dndsuite.core.json.parsers.Condition;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.exceptions.ConditionMismatchException;
import com.dndsuite.exceptions.JSONFormatException;

/**
 * ParentHasTag is a derivation of Condition. This Condition serves to determine
 * whether the parent Event of a Subevent contains a particular tag.
 * 
 * @author Calvin Withun
 *
 */
public class ParentEventHasTag implements Condition {

	@Override
	public boolean parse(JSONObject json, Effect e, Subevent s) throws ConditionMismatchException, JSONFormatException {
		if (!(json.containsKey("condition") && json.containsKey("tag"))) {
			throw new JSONFormatException();
		}
		String condition = (String) json.get("condition");
		if (!condition.equals("parent_event_has_tag")) {
			throw new ConditionMismatchException("parent_event_has_tag", condition);
		}

		String tag = (String) json.get("tag");
		return s.getParentEvent().hasTag(tag);
	}

}
