package com.dndsuite.core;

import org.json.simple.JSONObject;

import com.dndsuite.core.json.JSONLoader;
import com.dndsuite.exceptions.UUIDNotAssignedException;

public class Item extends JSONLoader implements UUIDTableElement {

	public Item(JSONObject json) {
		super(json);
	}

	public Item(String file) {
		super("items/" + file);
	}

	@Override
	protected void parseResourceData() {
		// TODO Auto-generated method stub

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
		// TODO: equip stuff here
	}

	public void unequipBy(GameObject o) {
		// TODO: unequip stuff here, assuming success
	}

}
