package com.dndsuite.core.json.parsers.subevents;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.dndsuite.core.Event;
import com.dndsuite.core.GameObject;
import com.dndsuite.core.Item;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.exceptions.JSONFormatException;
import com.dndsuite.exceptions.SubeventMismatchException;
import com.dndsuite.maths.dice.DamageDiceGroup;
import com.dndsuite.maths.dice.Die;

public class ItemDamage extends Subevent {

	@SuppressWarnings("unchecked")
	@Override
	public void parse(JSONObject json, Event e, GameObject eSource, GameObject eTarget)
			throws SubeventMismatchException, JSONFormatException {
		parent = e;
		String subevent = (String) json.get("subevent");
		addTag(subevent);
		if (!subevent.equals("item_damage")) {
			throw new SubeventMismatchException("item_damage", subevent);
		}

		Subevent s = getBaseCollection(json, eSource);

		JSONObject sJson = new JSONObject();
		sJson.put("subevent", "damage_dice_collection");
		s.parse(sJson, e, eSource, eTarget);
	}

	@SuppressWarnings("unchecked")
	public DamageDiceCollection getBaseCollection(JSONObject json, GameObject source) throws JSONFormatException {
		DamageDiceCollection s = new DamageDiceCollection();
		Item item;

		if (json.get("hand").equals("mainhand")) {
			item = source.getMainhand();
			// TODO: incorporate mainhand ability modifier damage
			// figure out where to place check for offhand damage bonus via Effects
		} else if (json.get("hand").equals("offhand")) {
			item = source.getOffhand();
		} else {
			throw new JSONFormatException();
		}

		if (item == null) {
			s.addDamageDiceGroup(new DamageDiceGroup(1L, 4L, "bludgeoning"));
		} else {
			JSONArray damageList = item.getDamage();
			for (Object o : damageList) {
				JSONObject damage = (JSONObject) o;
				long dice = (long) damage.getOrDefault("dice", 0L);
				long size = (long) damage.getOrDefault("size", 0L);
				long bonus = (long) damage.getOrDefault("bonus", 0L);
				String damageType = (String) damage.get("damage_type");
				s.addDamageDiceGroup(new DamageDiceGroup(dice, size, bonus, damageType));
			}

			if (item.hasTag("versatile") && source.getMainhand() == source.getOffhand()) {
				for (DamageDiceGroup group : s.getDamageDice()) {
					for (Die d : group.getDice()) {
						d.upsize();
					}
				}
			}
		}

		return s;
	}

	@Override
	public ItemDamage clone() {
		ItemDamage clone = new ItemDamage();
		clone.parent = getParentEvent();
		return clone;
	}

}
