package com.dndsuite.core;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.dndsuite.core.json.JSONLoader;
import com.dndsuite.exceptions.UUIDNotAssignedException;

public class Task extends JSONLoader implements UUIDTableElement {

	public Task(JSONObject json) {
		super(json);
		UUIDTable.addToTable(this);
	}

	public Task(String file) {
		super("tasks/" + file);
		UUIDTable.addToTable(this);
	}

	@Override
	public long getUUID() throws UUIDNotAssignedException {
		if (json.containsKey("uuid")) {
			return (long) json.get("uuid");
		}
		throw new UUIDNotAssignedException(this);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void assignUUID(long uuid) {
		json.put("uuid", uuid);
	}

	@Override
	protected void parseResourceData() {
		// TODO Auto-generated method stub

	}

	public void invoke(GameObject invoker) {
		JSONArray eventGroups = (JSONArray) json.get("event_groups");
		for (Object o : eventGroups) {
			JSONObject eventGroup = (JSONObject) o;
			invoker.queueEventGroup(new EventGroup(eventGroup));
		}
	}

}
