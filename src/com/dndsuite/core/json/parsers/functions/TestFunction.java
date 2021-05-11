package com.dndsuite.core.json.parsers.functions;

import org.json.simple.JSONObject;

import com.dndsuite.core.effects.Effect;
import com.dndsuite.core.json.parsers.Function;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.exceptions.FunctionMismatchException;

public class TestFunction implements Function {

	@Override
	public void parse(JSONObject json, Effect effect, Subevent s) throws FunctionMismatchException {
		String function = (String) json.get("function");
		if (function != "test_function") {
			throw new FunctionMismatchException("test_function", function);
		}
		System.out.println("Test function complete!");
	}

}
