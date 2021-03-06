package com.dndsuite.core.event_groups;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.dndsuite.core.Event;
import com.dndsuite.core.GameObject;
import com.dndsuite.core.Item;
import com.dndsuite.core.Observer;
import com.dndsuite.core.Subject;
import com.dndsuite.exceptions.JSONFormatException;

/**
 * ItemAttackGroup is a derivation of EventGroup which represents the
 * alternative means by which a GameObject can use an arbitrary item to perform
 * an attack. This includes unarmed attacks, melee attacks, thrown attacks, and
 * ranged attacks, and accounts for the finesse item property. It does not
 * account for non-dex/str ability score-based item attacks on its own. Objects
 * of this class subscribe to a GameObject in order to be notified whenever the
 * GameObject changes the weapon it is wielding.
 * 
 * @author Calvin Withun
 *
 */
public class ItemAttackGroup extends EventGroup implements Observer {
	JSONObject json;

	/**
	 * ItemAttackGroup constructor.
	 * 
	 * @param json    - indicates which hand is used to make the attack (mainhand or
	 *                offhand), as well as the collection of Subevents which occur
	 *                upon a hit or a miss
	 * @param invoker - the GameObject which has queued this EventGroup
	 * @throws JSONFormatException thrown if the hand is not "mainhand" or "offhand"
	 */
	public ItemAttackGroup(JSONObject json, GameObject invoker) throws JSONFormatException {
		this.json = json;
		invoker.addObserver(this);
		parse(invoker);
	}

	/**
	 * This function parses the json data provided upon construction and determines
	 * the Item held by the GameObject. It then clears and re-populates the
	 * EventGroup's Event collection.
	 * 
	 * @param invoker - the GameObject which hasa queued this EventGroup
	 * @throws JSONFormatException if the hand is not "mainhand" or "offhand"
	 */
	@SuppressWarnings("unchecked")
	public void parse(GameObject invoker) throws JSONFormatException {
		JSONObject json = null;
		try {
			JSONParser parser = new JSONParser();
			String jsonString = this.json.toString();
			json = (JSONObject) parser.parse(jsonString);
		} catch (ParseException ex) {
			ex.printStackTrace();
			return;
		}

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
				subevent.put("hit", json.get("hit"));
				subevent.put("miss", json.get("miss"));
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
					subevent.put("hit", json.get("hit"));
					subevent.put("miss", json.get("miss"));
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
						subevent.put("hit", json.get("hit"));
						subevent.put("miss", json.get("miss"));
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
					subevent.put("hit", json.get("hit"));
					subevent.put("miss", json.get("miss"));
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
					subevent.put("hit", json.get("hit"));
					subevent.put("miss", json.get("miss"));
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
						subevent.put("hit", json.get("hit"));
						subevent.put("miss", json.get("miss"));
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
					subevent.put("hit", json.get("hit"));
					subevent.put("miss", json.get("miss"));
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
					subevent.put("hit", json.get("hit"));
					subevent.put("miss", json.get("miss"));
					events.add(new Event(itemAttack));
				}
			}

		} catch (ParseException ex) {
			ex.printStackTrace();
		}

	}

	@Override
	public void update(Subject s) {
		try {
			parse((GameObject) s);
		} catch (JSONFormatException ex) {
			ex.printStackTrace();
		}
	}

}
