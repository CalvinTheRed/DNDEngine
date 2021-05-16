package com.dndsuite.core.json.parsers.functions;

import org.json.simple.JSONObject;

import com.dndsuite.core.Effect;
import com.dndsuite.core.json.parsers.Function;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.exceptions.FunctionMismatchException;

public class GrantAdvantage implements Function {

	@Override
	public void parse(JSONObject json, Effect effect, Subevent s) throws FunctionMismatchException {
		String function = (String) json.get("function");
		if (!function.equals("grant_advantage")) {
			throw new FunctionMismatchException("grant_advantage", function);
		}
		s.addTag("advantage");
	}

}
