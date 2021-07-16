package com.dndsuite.core.json.parsers;

import org.json.simple.JSONObject;

import com.dndsuite.core.Effect;
import com.dndsuite.exceptions.ConditionMismatchException;
import com.dndsuite.exceptions.JSONFormatException;

/**
 * Condition is an interface which represents the ability to influence whether
 * an Effect can be applied to a Subevent. A Condition can examine a Subevent
 * and return true or false depending on whether the Subevent meets a particular
 * qualification.
 * 
 * @author Calvin Withun
 *
 */
public interface Condition {

	public abstract boolean parse(JSONObject json, Effect e, Subevent s)
			throws ConditionMismatchException, JSONFormatException;

}
