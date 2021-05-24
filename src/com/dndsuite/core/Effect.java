package com.dndsuite.core;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.dndsuite.core.json.JSONLoader;
import com.dndsuite.core.json.parsers.Condition;
import com.dndsuite.core.json.parsers.Function;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.core.json.parsers.conditions.DealsDamageType;
import com.dndsuite.core.json.parsers.conditions.HasTag;
import com.dndsuite.core.json.parsers.conditions.IsRollBelow;
import com.dndsuite.core.json.parsers.conditions.ParentHasTag;
import com.dndsuite.core.json.parsers.functions.GrantAdvantage;
import com.dndsuite.core.json.parsers.functions.GrantDisadvantage;
import com.dndsuite.exceptions.ConditionMismatchException;
import com.dndsuite.exceptions.FunctionMismatchException;
import com.dndsuite.exceptions.JSONFormatException;
import com.dndsuite.exceptions.UUIDDoesNotExistException;

/**
 * Effect is a class which represents any lingering change in behavior which is
 * applied to a GameObject. This may be something such as a bonus granted from a
 * spell, or being intoxicated from drinking a toxin, or having an extended
 * critical hit range in virtue of a class feature. An Effect is composed of
 * various filters, each of which is composed of several Conditions and
 * Functions. There is no data type associated with a filter in this library.
 * 
 * @author Calvin Withun
 *
 */
public class Effect extends JSONLoader implements UUIDTableElement {

	/**
	 * A HashMap dedicated to mapping all of the conditions which may be referenced
	 * by an Effect JSON file. Any condition not included in this HashMap cannot be
	 * referenced by an Effect JSON file.
	 */
	public static final Map<String, Condition> CONDITION_MAP = new HashMap<String, Condition>() {

		// TODO: design a way for the Slow spell to limit the number of AttackRoll
		// subevents that can be invoked on a turn

		private static final long serialVersionUID = -4761488814133689338L;

		{
			put("deals_damage_type", new DealsDamageType());
			put("has_tag", new HasTag());
			put("is_roll_below", new IsRollBelow());
			put("parent_event_has_tag", new ParentHasTag());
		}
	};

	/**
	 * A HashMap dedicated to mapping all of the functions which may be referenced
	 * by an Effect JSON file. Any function not included in this HashMap cannot be
	 * referenced by an Effect JSON file.
	 */
	public static final Map<String, Function> FUNCTION_MAP = new HashMap<String, Function>() {

		private static final long serialVersionUID = -6652098870662435261L;

		{
			put("grant_advantage", new GrantAdvantage());
			put("grant_disadvantage", new GrantDisadvantage());
		}
	};

	/**
	 * Effect constructor for loading from save JSON files.
	 * 
	 * @param json - the JSON data stored in a save file
	 */
	public Effect(JSONObject json) {
		super(json);

		UUIDTable.addToTable(this);
	}

	/**
	 * Effect constructor for loading an Effect from template JSON files.
	 * 
	 * @param file   - the path to a file, as a continuation of the file path
	 *               "resources/json/effects/..."
	 * @param source - the GameObject which created this Effect
	 * @param target - the GameObject which this Effect is applied to
	 */
	@SuppressWarnings("unchecked")
	public Effect(String file, GameObject source, GameObject target) {
		super("effects/" + file);

		json.put("source", source.getUUID());
		json.put("target", target.getUUID());

		UUIDTable.addToTable(this);
	}

	@Override
	protected void parseTemplate() {
		// TODO Auto-generated method stub

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
	 * This function allows an Effect to assess and modify (if appropriate) a
	 * Subevent in order to change or influence its outcome, according to the
	 * Effect's filters. An Event cannot be applied to a Subevent if that Subevent
	 * has already been modified by an Effect with the same name.
	 * 
	 * @param s - the Subevent to be evaluated
	 * @return true if s was changed, false if s was not changed
	 */
	public boolean processSubevent(Subevent s) {
		JSONArray filters = (JSONArray) json.get("filters");
		for (int i = 0; i < filters.size(); i++) {
			JSONObject filter = (JSONObject) filters.get(i);

			JSONArray conditions = (JSONArray) filter.get("conditions");
			boolean conditionsMet = true;
			try {
				for (int j = 0; j < conditions.size(); j++) {
					JSONObject condition = (JSONObject) conditions.get(j);
					conditionsMet &= CONDITION_MAP.get(condition.get("condition")).parse(condition, this, s);
				}
			} catch (ConditionMismatchException ex) {
				ex.printStackTrace();
			} catch (JSONFormatException ex) {
				ex.printStackTrace();
			}

			if (conditionsMet) {
				try {
					s.applyEffect(this);
				} catch (Exception ex) {
					System.out.println(this + " already applied to " + s);
					return false;
				}

				JSONArray functions = (JSONArray) filter.get("functions");
				try {
					for (int j = 0; j < functions.size(); j++) {
						JSONObject function = (JSONObject) functions.get(j);
						FUNCTION_MAP.get(function.get("function")).parse(function, this, s);
					}
					return true;
				} catch (FunctionMismatchException ex) {
					ex.printStackTrace();
				} catch (JSONFormatException ex) {
					ex.printStackTrace();
				}
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public void setSource(GameObject o) {
		json.put("source", o.getUUID());
	}

	@SuppressWarnings("unchecked")
	public void setTarget(GameObject o) {
		json.put("target", o.getUUID());
	}

	public GameObject getSource() {
		try {
			return (GameObject) UUIDTable.get((long) json.get("source"));
		} catch (UUIDDoesNotExistException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public GameObject getTarget() {
		try {
			return (GameObject) UUIDTable.get((long) json.get("target"));
		} catch (UUIDDoesNotExistException ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
