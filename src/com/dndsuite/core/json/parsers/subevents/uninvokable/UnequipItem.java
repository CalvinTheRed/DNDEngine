package com.dndsuite.core.json.parsers.subevents.uninvokable;

import org.json.simple.JSONObject;

import com.dndsuite.core.Event;
import com.dndsuite.core.GameObject;
import com.dndsuite.core.Item;
import com.dndsuite.core.UUIDTable;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.exceptions.JSONFormatException;
import com.dndsuite.exceptions.SubeventMismatchException;
import com.dndsuite.exceptions.UUIDDoesNotExistException;

/**
 * UnequipItem is a derivation of Subevent which cannot be invoked from within a
 * JSON file. This Subevent is responsible for determining whether a GameObject
 * is capable of un-equipping an Item. Some Items may be cursed such that they
 * cannot be removed by normal means.
 * 
 * @author Calvin Withun
 *
 */
public class UnequipItem extends Subevent {
	protected Item item;
	protected boolean prevented;
	protected boolean permitted;

	@Override
	public void parse(JSONObject json, Event e, GameObject eSource, GameObject eTarget)
			throws SubeventMismatchException, JSONFormatException {
		super.parse(json, e, eSource, eTarget);
		String subevent = (String) json.get("subevent");
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
		clone.prevented = prevented;
		clone.permitted = permitted;
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
