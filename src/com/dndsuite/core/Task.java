package com.dndsuite.core;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.dndsuite.core.event_groups.EventGroup;
import com.dndsuite.core.event_groups.ItemAttackGroup;
import com.dndsuite.core.json.JSONLoader;
import com.dndsuite.exceptions.JSONFormatException;
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
	protected void parseTemplate() {
		// TODO Auto-generated method stub
	}

	public void invoke(GameObject invoker) throws JSONFormatException {
		if (json.containsKey("event_groups")) {
			JSONArray eventGroups = (JSONArray) json.get("event_groups");
			for (Object o : eventGroups) {
				JSONObject eventGroup = (JSONObject) o;
				invoker.queueEventGroup(new EventGroup(eventGroup));
			}
		}

		if (json.containsKey("item_attack_groups")) {
			JSONArray itemAttackGroups = (JSONArray) json.get("item_attack_groups");
			for (Object o : itemAttackGroups) {
				JSONObject itemAttackGroupData = (JSONObject) o;
				invoker.queueEventGroup(new ItemAttackGroup(itemAttackGroupData, invoker));
			}
		}

	}

}
