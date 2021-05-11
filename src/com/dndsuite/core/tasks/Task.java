package com.dndsuite.core.tasks;

import org.json.simple.JSONObject;

import com.dndsuite.core.json.JSONLoader;

public class Task extends JSONLoader {

	public Task(JSONObject json) {
		super(json);
	}

	public Task(String file) {
		super("tasks/" + file);
	}

	@Override
	protected void parseBasePattern() {
		// TODO Auto-generated method stub

	}

}
