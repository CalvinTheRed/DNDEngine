package com.dndsuite.core.json.parsers.subevents.uninvokable;

import org.json.simple.JSONObject;

import com.dndsuite.core.Event;
import com.dndsuite.core.GameObject;
import com.dndsuite.core.Item;
import com.dndsuite.core.UUIDTable;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.exceptions.SubeventMismatchException;
import com.dndsuite.exceptions.UUIDDoesNotExistException;

public class UnequipItem extends Subevent {
	protected Item item;
	protected boolean prevented;
	protected boolean permitted;

	@Override
	public void parse(JSONObject json, Event e, GameObject eSource, GameObject eTarget)
			throws SubeventMismatchException {
		parent = e;
		String subevent = (String) json.get("subevent");
		addTag(subevent);
		if (!subevent.equals("unequip_item")) {
			throw new SubeventMismatchException("unequip_item", subevent);
		}

		try {
			prevented = false;
			permitted = false;
			item = (Item) UUIDTable.get((long) json.get("item_uuid"));
			presentToEffects(eSource, eTarget);
		} catch (UUIDDoesNotExistException ex) {
			ex.printStackTrace();
		}

	}

	@Override
	public UnequipItem clone() {
		UnequipItem clone = new UnequipItem();
		clone.item = item;
		return clone;
	}

	public Item getItem() {
		return item;
	}

	public void permit() {
		permitted = true;
	}

	public void prevent() {
		prevented = true;
	}

	public boolean getSuccess() {
		if (prevented && !permitted) {
			return false;
		}
		return true;
	}

}
