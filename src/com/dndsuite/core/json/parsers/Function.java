package com.dndsuite.core.json.parsers;

import org.json.simple.JSONObject;

import com.dndsuite.core.effects.Effect;
import com.dndsuite.exceptions.FunctionMismatchException;

public interface Function {

	public abstract void parse(JSONObject json, Effect effect, Subevent s) throws FunctionMismatchException;

}
