package com.dndsuite.core.event_groups;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.dndsuite.core.Event;
import com.dndsuite.core.GameObject;
import com.dndsuite.core.Item;
import com.dndsuite.exceptions.JSONFormatException;

public class ItemAttackGroup extends EventGroup {

	public ItemAttackGroup(JSONObject json, GameObject invoker) throws JSONFormatException {
		parse(json, invoker);
	}

	@SuppressWarnings("unchecked")
	public void parse(JSONObject json, GameObject invoker) throws JSONFormatException {
		String hand = (String) json.get("hand");
		Item item;

		if (hand.equals("mainhand")) {
			item = invoker.getMainhand();
		} else if (hand.equals("offhand")) {
			item = invoker.getOffhand();
		} else {
			throw new JSONFormatException();
		}

		try {
			events.clear();
			JSONParser parser = new JSONParser();
			String jsonString;
			JSONObject itemAttack;
			JSONArray subevents;
			JSONObject subevent;
			JSONObject areaOfEffect;
			if (item == null) {
				// unarmed strike options
				jsonString = "{\"name\":\"Unarmed Strike\",\"description\":\"...\",\"tags\":[\"unarmed_strike\",\"melee_attack\"],"
						+ "\"area_of_effect\":{\"shape\":\"single_target\",\"range\":5.0},\"damage\":[{\"bonus\":1,\"damage_type\":\"bludgeoning\"}],"
						+ "\"subevents\":[{\"subevent\":\"attack_roll\",\"attack_ability\":\"str\",\"hit\":[],\"miss\":[]}]}";
				itemAttack = (JSONObject) parser.parse(jsonString);
				subevents = (JSONArray) itemAttack.get("subevents");
				subevent = (JSONObject) subevents.get(0);
				addHitAndMiss(subevent, json);
				events.add(new Event(itemAttack));
			} else {
				if (item.hasTag("melee")) {
					// melee attack options
					jsonString = "{\"name\":\"Melee Attack\",\"description\":\"...\",\"tags\":[\"melee_attack\",\"item_attack\"],"
							+ "\"area_of_effect\":{\"shape\":\"single_target\",\"range\":5.0},\"damage\":[],"
							+ "\"subevents\":[{\"subevent\":\"attack_roll\",\"attack_ability\":\"str\",\"hit\":[],\"miss\":[]}]}";
					itemAttack = (JSONObject) parser.parse(jsonString);
					itemAttack.put("damage", item.getDamage());
					subevents = (JSONArray) itemAttack.get("subevents");
					subevent = (JSONObject) subevents.get(0);
					addHitAndMiss(subevent, json);
					events.add(new Event(itemAttack));

					if (item.hasTag("finesse")) {
						// finesse melee weapons get dex-based attacks
						jsonString = "{\"name\":\"Melee Attack\",\"description\":\"...\",\"tags\":[\"melee_attack\",\"item_attack\"],"
								+ "\"area_of_effect\":{\"shape\":\"single_target\",\"range\":5.0},\"damage\":[],"
								+ "\"subevents\":[{\"subevent\":\"attack_roll\",\"attack_ability\":\"dex\",\"hit\":[],\"miss\":[]}]}";
						itemAttack = (JSONObject) parser.parse(jsonString);
						itemAttack.put("damage", item.getDamage());
						subevents = (JSONArray) itemAttack.get("subevents");
						subevent = (JSONObject) subevents.get(0);
						addHitAndMiss(subevent, json);
						events.add(new Event(itemAttack));
					}
				} else {
					// improvised melee attack options
					jsonString = "{\"name\":\"Melee Attack\",\"description\":\"...\",\"tags\":[\"melee_attack\",\"improvised\",\"item_attack\"],"
							+ "\"area_of_effect\":{\"shape\":\"single_target\",\"range\":5.0},\"damage\":[{\"dice\":1,\"size\":4,\"damage_type\":\"bludgeoning\"}],"
							+ "\"subevents\":[{\"subevent\":\"attack_roll\",\"attack_ability\":\"str\",\"hit\":[],\"miss\":[]}]}";
					itemAttack = (JSONObject) parser.parse(jsonString);
					subevents = (JSONArray) itemAttack.get("subevents");
					subevent = (JSONObject) subevents.get(0);
					addHitAndMiss(subevent, json);
					events.add(new Event(itemAttack));
				}

				if (item.hasTag("thrown")) {
					// thrown attack options
					jsonString = "{\"name\":\"Thrown Attack\",\"description\":\"...\",\"tags\":[\"thrown_attack\",\"item_attack\"],"
							+ "\"area_of_effect\":{\"shape\":\"single_target\",\"range\":{}},\"damage\":[],"
							+ "\"subevents\":[{\"subevent\":\"attack_roll\",\"attack_ability\":\"str\",\"hit\":[],\"miss\":[]}]}";
					itemAttack = (JSONObject) parser.parse(jsonString);
					itemAttack.put("damage", item.getDamage());
					areaOfEffect = (JSONObject) itemAttack.get("area_of_effect");
					areaOfEffect.put("range", item.getRange());
					subevents = (JSONArray) itemAttack.get("subevents");
					subevent = (JSONObject) subevents.get(0);
					addHitAndMiss(subevent, json);
					events.add(new Event(itemAttack));

					if (item.hasTag("finesse")) {
						// finesse thrown weapons get dex-based attacks
						jsonString = "{\"name\":\"Thrown Attack\",\"description\":\"...\",\"tags\":[\"thrown_attack\",\"item_attack\"],"
								+ "\"area_of_effect\":{\"shape\":\"single_target\",\"range\":{}},\"damage\":[],"
								+ "\"subevents\":[{\"subevent\":\"attack_roll\",\"attack_ability\":\"dex\",\"hit\":[],\"miss\":[]}]}";
						itemAttack = (JSONObject) parser.parse(jsonString);
						itemAttack.put("damage", item.getDamage());
						areaOfEffect = (JSONObject) itemAttack.get("area_of_effect");
						areaOfEffect.put("range", item.getRange());
						subevents = (JSONArray) itemAttack.get("subevents");
						subevent = (JSONObject) subevents.get(0);
						addHitAndMiss(subevent, json);
						events.add(new Event(itemAttack));
					}
				} else {
					// improvised thrown attack options
					jsonString = "{\"name\":\"Thrown Attack\",\"description\":\"...\",\"tags\":[\"thrown_attack\",\"improvised\",\"item_attack\"],"
							+ "\"area_of_effect\":{\"shape\":\"single_target\",\"range\":{\"short\":20.0,\"long\":60.0}},\"damage\":[{\"dice\":1,\"size\":4,\"damage_type\":\"bludgeoning\"}],"
							+ "\"subevents\":[{\"subevent\":\"attack_roll\",\"attack_ability\":\"str\",\"hit\":[],\"miss\":[]}]}";
					itemAttack = (JSONObject) parser.parse(jsonString);
					subevents = (JSONArray) itemAttack.get("subevents");
					subevent = (JSONObject) subevents.get(0);
					addHitAndMiss(subevent, json);
					events.add(new Event(itemAttack));
				}

				if (item.hasTag("ranged")) {
					// ranged attack options
					jsonString = "{\"name\":\"Ranged Attack\",\"description\":\"...\",\"tags\":[\"ranged_attack\",\"item_attack\"],"
							+ "\"area_of_effect\":{\"shape\":\"single_target\",\"range\":{}},\"damage\":[],"
							+ "\"subevents\":[{\"subevent\":\"attack_roll\",\"attack_ability\":\"dex\",\"hit\":[],\"miss\":[]}]}";
					itemAttack = (JSONObject) parser.parse(jsonString);
					itemAttack.put("damage", item.getDamage());
					areaOfEffect = (JSONObject) itemAttack.get("area_of_effect");
					areaOfEffect.put("range", item.getRange());
					subevents = (JSONArray) itemAttack.get("subevents");
					subevent = (JSONObject) subevents.get(0);
					addHitAndMiss(subevent, json);
					events.add(new Event(itemAttack));
				}
			}

		} catch (ParseException ex) {
			ex.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	private void addHitAndMiss(JSONObject subevent, JSONObject json) {
		subevent.put("hit", json.get("hit"));
		subevent.put("miss", json.get("miss"));
	}

}
