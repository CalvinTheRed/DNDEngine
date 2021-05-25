package com.dndsuite.core.json.parsers.functions;

import org.json.simple.JSONObject;

import com.dndsuite.core.Effect;
import com.dndsuite.core.json.parsers.Function;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.exceptions.FunctionMismatchException;
import com.dndsuite.exceptions.JSONFormatException;

/**
 * GrantAdvantage is a derivation of Function. This Function grants the
 * "advantage" tag to a Subevent (regardless of whether the Subevent entails the
 * rolling of a 20-sided Die).
 * 
 * @author Calvin Withun
 *
 */
public class GrantAdvantage implements Function {

	@Override
	public void parse(JSONObject json, Effect effect, Subevent s)
			throws FunctionMismatchException, JSONFormatException {
		if (!(json.containsKey("function"))) {
			throw new JSONFormatException();
		}
		String function = (String) json.get("function");
		if (!function.equals("grant_advantage")) {
			throw new FunctionMismatchException("grant_advantage", function);
		}

		s.addTag("advantage");
	}

}
