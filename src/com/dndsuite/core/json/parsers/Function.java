package com.dndsuite.core.json.parsers;

import org.json.simple.JSONObject;

import com.dndsuite.core.Effect;
import com.dndsuite.exceptions.FunctionMismatchException;
import com.dndsuite.exceptions.JSONFormatException;

public interface Function {

	public abstract void parse(JSONObject json, Effect effect, Subevent s)
			throws FunctionMismatchException, JSONFormatException;

}
