package com.dndsuite.core;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.dndsuite.core.json.JSONLoader;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.core.json.parsers.subevents.invokable.ApplyEffect;
import com.dndsuite.core.json.parsers.subevents.invokable.AttackRoll;
import com.dndsuite.core.json.parsers.subevents.invokable.Damage;
import com.dndsuite.core.json.parsers.subevents.invokable.ItemDamage;
import com.dndsuite.core.json.parsers.subevents.invokable.SavingThrow;
import com.dndsuite.core.json.parsers.subevents.uninvokable.DamageDiceCollection;
import com.dndsuite.exceptions.InvalidAreaOfEffectException;
import com.dndsuite.exceptions.JSONFormatException;
import com.dndsuite.exceptions.OutOfRangeException;
import com.dndsuite.exceptions.SubeventMismatchException;
import com.dndsuite.maths.Vector;
import com.dndsuite.maths.dice.DamageDiceGroup;

public class Event extends JSONLoader implements Receptor {

	public static final Map<String, Subevent> SUBEVENT_MAP = new HashMap<String, Subevent>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = -5295424280559257214L;

		{
			put("apply_effect", new ApplyEffect());
			put("attack_roll", new AttackRoll());
			put("damage", new Damage());
			put("item_damage", new ItemDamage());
			put("saving_throw", new SavingThrow());
		}
	};

	protected DamageDiceCollection baseDiceCollection;
	protected JSONObject pauseNotes;

	/**
	 * Not to be used in client program
	 * 
	 * @param json
	 */
	public Event(JSONObject json) {
		super(json);
	}

	public Event(String file) {
		super("events/" + file);
	}

	@Override
	protected void parseResourceData() {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	public void invoke(Vector start, Vector end, GameObject source) {
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
				for (GameObject target : VirtualBoard.objectsInAreaOfEffect(source.getPos(), start, end, json)) {
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
	public void pause(JSONObject pauseNotes) {
		this.pauseNotes = pauseNotes;
		ReceptorQueue.enqueue(this);

	}

	@Override
	public void resume(JSONObject json) throws JSONFormatException {
		if (json.containsKey("start") && json.containsKey("end")) {
			JSONArray start = (JSONArray) json.get("start");
			JSONArray end = (JSONArray) json.get("end");
			if (start.size() == 3 && end.size() == 3) {
				Vector startVector = new Vector((double) start.get(0), (double) start.get(1), (double) start.get(2));
				Vector endVector = new Vector((double) end.get(0), (double) end.get(1), (double) end.get(2));
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
