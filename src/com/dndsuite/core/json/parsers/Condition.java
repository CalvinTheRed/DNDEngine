package com.dndsuite.core.json.parsers;

import org.json.simple.JSONObject;

import com.dndsuite.core.Effect;
import com.dndsuite.exceptions.ConditionMismatchException;
import com.dndsuite.exceptions.JSONFormatException;

public interface Condition {

	public abstract boolean parse(JSONObject json, Effect e, Subevent s)
			throws ConditionMismatchException, JSONFormatException;

}
