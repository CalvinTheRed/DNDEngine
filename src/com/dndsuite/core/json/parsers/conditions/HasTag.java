package com.dndsuite.core.json.parsers.conditions;

import org.json.simple.JSONObject;

import com.dndsuite.core.Effect;
import com.dndsuite.core.json.parsers.Condition;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.exceptions.ConditionMismatchException;

/**
 * HasTag is a derivation of Condition. This Condition serves to determine
 * whether a Subevent contains a particular tag.
 * 
 * @author Calvin Withun
 *
 */
public class HasTag implements Condition {

	@Override
	public boolean parse(JSONObject json, Effect e, Subevent s) throws ConditionMismatchException {
		String condition = (String) json.get("condition");
		if (!condition.equals("has_tag")) {
			throw new ConditionMismatchException("has_tag", condition);
		}

		String tag = (String) json.get("tag");
		return s.hasTag(tag);
	}

}
