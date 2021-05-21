package com.dndsuite.core;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.dndsuite.core.json.JSONLoader;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.core.json.parsers.subevents.invokable.ApplyEffect;
import com.dndsuite.core.json.parsers.subevents.invokable.AttackRoll;
import com.dndsuite.core.json.parsers.subevents.invokable.Damage;
import com.dndsuite.core.json.parsers.subevents.invokable.SavingThrow;
import com.dndsuite.core.json.parsers.subevents.uninvokable.DamageDiceCollection;
import com.dndsuite.exceptions.InvalidAreaOfEffectException;
import com.dndsuite.exceptions.JSONFormatException;
import com.dndsuite.exceptions.OutOfRangeException;
import com.dndsuite.exceptions.SubeventMismatchException;
import com.dndsuite.maths.Vector;
import com.dndsuite.maths.dice.DamageDiceGroup;

/**
 * Event is a class which represents a particular activity which can be
 * performed by a GameObject. This may be something such as casting a spell, or
 * using an Item to attack, or performing a skill check. An Event is composed of
 * a collection of Subevents, each of which represent one distinct aspect of the
 * Event.
 * 
 * @author Calvin Withun
 *
 */
public class Event extends JSONLoader implements Receptor {

	/**
	 * A HashMap dedicated to mapping all of the Subevents which may be referenced
	 * by an Event JSON file. Any Subevent not included in this HashMap cannot be
	 * directly referenced by an Event JSON file. However, they may still be
	 * assessed by Conditions or modified by Functions in an Effect JSON file.
	 */
	public static final Map<String, Subevent> SUBEVENT_MAP = new HashMap<String, Subevent>() {

		private static final long serialVersionUID = -5295424280559257214L;

		{
			put("apply_effect", new ApplyEffect());
			put("attack_roll", new AttackRoll());
			put("damage", new Damage());
			put("saving_throw", new SavingThrow());
		}
	};

	protected DamageDiceCollection baseDiceCollection;
	protected JSONObject pauseNotes;

	/**
	 * Event constructor for loading from save JSON files. Such files are not
	 * currently intended to exist; this constructor exists primarily for running
	 * tests.
	 * 
	 * @param json - the JSON data stored in a save file
	 */
	public Event(JSONObject json) {
		super(json);

		try {
			JSONParser parser = new JSONParser();
			String jsonString = "{\"requests\":[\"event_target_data\"]}";
			pauseNotes = (JSONObject) parser.parse(jsonString);
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Event constructor for loading an Event from template JSON files.
	 * 
	 * @param file - the path to a file, as a continuation of the file path
	 *             "resources/json/events/..."
	 */
	public Event(String file) {
		super("events/" + file);

		try {
			JSONParser parser = new JSONParser();
			String jsonString = "{\"requests\":[\"event_target_data\"]}";
			pauseNotes = (JSONObject) parser.parse(jsonString);
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	protected void parseTemplate() {
		// TODO Auto-generated method stub

	}

	/**
	 * This function precipitates the consequences of the Event. Each Subevent
	 * defined in the Event JSON file will be parsed and processed in order.
	 * 
	 * @param start   - the point at which the Event is determined to begin, if
	 *                applicable
	 * @param pointTo - a point at which the Event is directed, if applicable
	 * @param source  - the GameObject which is enacting the Event
	 */
	@SuppressWarnings("unchecked")
	public void invoke(Vector start, Vector pointTo, GameObject source) {
		// generate base damage subevent and r
		baseDiceCollection = new DamageDiceCollection();
		JSONArray damageList = (JSONArray) json.getOrDefault("damage", new JSONArray());
		for (Object o : damageList) {
			JSONObject damageElement = (JSONObject) o;
			long dice = (long) damageElement.getOrDefault("dice", 0L);
			long size = (long) damageElement.getOrDefault("size", 0L);
			long bonus = (long) damageElement.getOrDefault("bonus", 0L);
			String damageType = (String) damageElement.get("damage_type");
			DamageDiceGroup group = new DamageDiceGroup(dice, size, damageType);
			group.addBonus(bonus);
			baseDiceCollection.addDamageDiceGroup(group);
		}

		// iterate through subevents
		JSONArray subevents = (JSONArray) json.get("subevents");
		for (Object o : subevents) {
			JSONObject subevent = (JSONObject) o;
			try {
				for (GameObject target : VirtualBoard.objectsInAreaOfEffect(source.getPos(), start, pointTo, json)) {
					SUBEVENT_MAP.get(subevent.get("subevent")).clone().parse(subevent, this, source, target);
				}
			} catch (SubeventMismatchException ex) {
				ex.printStackTrace();
			} catch (InvalidAreaOfEffectException ex) {
				ex.printStackTrace();
			} catch (OutOfRangeException ex) {
				ex.printStackTrace();
				// TODO: verify that the Event does not become expended in this case
			} catch (JSONFormatException ex) {
				ex.printStackTrace();
			}
		}
	}

	@Override
	public Event clone() {
		return new Event(json);
	}

	public DamageDiceCollection getBaseDiceCollection() {
		return baseDiceCollection;
	}

	@Override
	public void pause() {
		ReceptorQueue.enqueue(this);

	}

	@Override
	public void resume(JSONObject json) throws JSONFormatException {
		JSONArray responses = (JSONArray) json.get("responses");
		JSONObject response = (JSONObject) responses.get(0);

		if (response.containsKey("start") && response.containsKey("point_to")) {
			JSONArray start = (JSONArray) response.get("start");
			JSONArray pointTo = (JSONArray) response.get("end");
			if (start.size() == 3 && pointTo.size() == 3) {
				Vector startVector = new Vector((double) start.get(0), (double) start.get(1), (double) start.get(2));
				Vector endVector = new Vector((double) pointTo.get(0), (double) pointTo.get(1),
						(double) pointTo.get(2));
				invoke(startVector, endVector, (GameObject) pauseNotes.get("source"));
			} else {
				throw new JSONFormatException();
			}
		} else {
			throw new JSONFormatException();
		}
	}

	@Override
	public JSONObject getPauseNotes() {
		return pauseNotes;
	}

}
