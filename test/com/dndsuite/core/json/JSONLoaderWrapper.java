package com.dndsuite.core.json;

import org.json.simple.JSONObject;

public class JSONLoaderWrapper extends JSONLoader {

	public JSONLoaderWrapper(JSONObject json) {
		super(json);
	}

	public JSONLoaderWrapper(String file) {
		super(file);
	}

	@Override
	protected void parseBasePattern() {
	}

}
