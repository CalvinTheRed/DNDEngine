package com.dndsuite.core;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.dndsuite.core.json.JSONLoader;
import com.dndsuite.exceptions.UUIDDoesNotExistException;
import com.dndsuite.exceptions.UUIDNotAssignedException;

public class Item extends JSONLoader implements UUIDTableElement {

	public Item(JSONObject json) {
		super(json);

		UUIDTable.addToTable(this);
	}

	public Item(String file) {
		super("items/" + file);

		UUIDTable.addToTable(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void parseTemplate() {
		JSONArray effectNames = (JSONArray) json.remove("equipped_effects");
		JSONArray effectUUIDs = new JSONArray();
		while (effectNames.size() > 0) {
			// TODO: ensure source/target UUIDs are provided here
			String effectName = (String) effectNames.remove(0);
			// TODO: does null, null work?
			Effect e = new Effect(effectName, null, null);
			UUIDTable.addToTable(e);
			try {
				effectUUIDs.add(e.getUUID());
			} catch (UUIDNotAssignedException ex) {
				ex.printStackTrace();
			}

		}
		json.put("equipped_effects", effectUUIDs);
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
