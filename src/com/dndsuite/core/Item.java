package com.dndsuite.core;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.dndsuite.core.json.JSONLoader;
import com.dndsuite.exceptions.UUIDDoesNotExistException;

/**
 * Item is a class which represents any physical artifact to be found in the
 * game. This might be a weapon, or a tool of some kind, or a random natural
 * object such as a small rock.
 * 
 * @author Calvin Withun
 *
 */
public class Item extends JSONLoader implements UUIDTableElement {

	/**
	 * Item constructor for loading from save JSON files.
	 * 
	 * @param json - the JSON data stored in a save file
	 */
	public Item(JSONObject json) {
		super(json);

		UUIDTable.addToTable(this);
	}

	/**
	 * Item constructor for loading an Item from template JSON files.
	 * 
	 * @param file - the path to a file, as a continuation of the file path
	 *             "resources/json/items/..."
	 */
	public Item(String file) {
		super("items/" + file);

		UUIDTable.addToTable(this);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void parseTemplate() {
		JSONArray effectNames = (JSONArray) json.remove("equipped_effects");
		JSONArray effectUUIDs = new JSONArray();
		while (effectNames.size() > 0) {
			// TODO: ensure source/target UUIDs are provided here
			String effectName = (String) effectNames.remove(0);
			// TODO: does null, null work?
			Effect e = new Effect(effectName, null, null);
			UUIDTable.addToTable(e);
			effectUUIDs.add(e.getUUID());
		}
		json.put("equipped_effects", effectUUIDs);
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

	/**
	 * This function gives the Item a chance to apply Effects to its wielder upon
	 * being equipped.
	 * 
	 * @param o - the GameObject which has equipped the Item
	 */
	public void equipBy(GameObject o) {
		try {
			JSONArray equippedEffects = (JSONArray) json.get("equipped_effects");
			for (Object obj : equippedEffects) {
				long uuid = (long) obj;
				Effect e = (Effect) UUIDTable.get(uuid);
				e.setSource(o);
				e.setTarget(o);
				o.addEffect(e);
			}
		} catch (UUIDDoesNotExistException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * This function gives the Item a chance to remove Effects from its wielder upon
	 * being unequipped.
	 * 
	 * @param o - the GameObject which has unequipped the Item
	 */
	public void unequipBy(GameObject o) {
		try {
			JSONArray equippedEffects = (JSONArray) json.get("equipped_effects");
			for (Object obj : equippedEffects) {
				long uuid = (long) obj;
				Effect e = (Effect) UUIDTable.get(uuid);
				e.setSource(null);
				e.setTarget(null);
				o.removeEffect(e);
			}
		} catch (UUIDDoesNotExistException ex) {
			ex.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public JSONArray getDamage() {
		JSONArray improvised = new JSONArray();
		JSONObject damage = new JSONObject();
		damage.put("dice", 1L);
		damage.put("size", 4L);
		damage.put("damage_type", "bludgeoning");
		improvised.add(damage);
		return (JSONArray) json.getOrDefault("damage", improvised);
	}

	@SuppressWarnings("unchecked")
	public JSONObject getRange() {
		JSONObject range = new JSONObject();
		range.put("short", 20.0);
		range.put("long", 60.0);
		return (JSONObject) json.getOrDefault("range", range);
	}

}
