package com.dndsuite.core;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.dndsuite.core.event_groups.EventGroup;
import com.dndsuite.core.event_groups.ItemAttackGroup;
import com.dndsuite.core.json.JSONLoader;
import com.dndsuite.exceptions.JSONFormatException;

/**
 * Task is a class which represents the exchange between a GameObject's
 * resources and its ability to do things in the game. For example, for a first
 * level player to swing a sword as an attack, they must spend an action. A Task
 * is composed of a collection of EventGroups.
 * 
 * @author calvi
 *
 */
public class Task extends JSONLoader implements UUIDTableElement {

	/**
	 * Task constructor for loading from save JSON files.
	 * 
	 * @param json - the JSON data stored in a save file
	 */
	public Task(JSONObject json) {
		super(json);

		UUIDTable.addToTable(this);
	}

	/**
	 * Task constructor for loading a Task from template JSON files.
	 * 
	 * @param file - the path to a file, as a continuation of the file path
	 *             "resources/json/tasks/..."
	 */
	public Task(String file) {
		super("tasks/" + file);

		UUIDTable.addToTable(this);
	}

	@Override
	@SuppressWarnings("unchecked")
	public long getUUID() {
		return (long) json.getOrDefault("uuid", -1L);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void assignUUID(long uuid) {
		json.put("uuid", uuid);
	}

	@Override
	protected void parseTemplate() {
	}

	/**
	 * This function is the exchanging of resources for EventGroups which can be
	 * used at a later time to take actions.
	 * 
	 * @param invoker - the GameObject which is invoking the Task
	 * @throws JSONFormatException if the Task involves ItemAttackGroups rather than
	 *                             standard EventGroups, and the hand which is used
	 *                             to make the Item attacks is not indicated, this
	 *                             exception will be thrown.
	 */
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
