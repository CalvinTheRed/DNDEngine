package com.dndsuite.core.json.parsers;

import org.json.simple.JSONObject;

import com.dndsuite.core.effects.Effect;
import com.dndsuite.exceptions.ConditionMismatchException;

public interface Condition {

	public abstract boolean parse(JSONObject json, Effect e, Subevent s) throws ConditionMismatchException;

}
