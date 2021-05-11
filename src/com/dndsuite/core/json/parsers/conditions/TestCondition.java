package com.dndsuite.core.json.parsers.conditions;

import org.json.simple.JSONObject;

import com.dndsuite.core.effects.Effect;
import com.dndsuite.core.json.parsers.Condition;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.exceptions.ConditionMismatchException;

public class TestCondition implements Condition {

	@Override
	public boolean parse(JSONObject json, Effect e, Subevent s) throws ConditionMismatchException {
		String condition = (String) json.get("condition");
		if (condition != "test_condition") {
			throw new ConditionMismatchException("test_condition", condition);
		}
		System.out.println("Test condition evaluated to: true!");
		return true;
	}

}
