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
import com.dndsuite.exceptions.UUIDNotAssignedException;

public class Effect extends JSONLoader implements UUIDTableElement {

	public static final Map<String, Condition> CONDITION_MAP = new HashMap<String, Condition>() {

		// TODO: design a way for the Slow spell to limit the number of AttackRoll
		// subevents that can be invoked on a turn

		/**
		 * 
		 */
		private static final long serialVersionUID = -4761488814133689338L;

		{
			put("deals_damage_type", new DealsDamageType());
			put("has_tag", new HasTag());
			put("is_roll_below", new IsRollBelow());
			put("parent_event_has_tag", new ParentHasTag());
		}
	};

	public static final Map<String, Function> FUNCTION_MAP = new HashMap<String, Function>() {

		/**
		 * 
		 */
		private static final long serialVersionUID = -6652098870662435261L;

		{
			put("grant_advantage", new GrantAdvantage());
			put("grant_disadvantage", new GrantDisadvantage());
		}
	};

	public Effect(JSONObject json) {
		super(json);

		UUIDTable.addToTable(this);
	}

	@SuppressWarnings("unchecked")
	public Effect(String file, GameObject source, GameObject target) {
		super("effects/" + file);

		try {
			json.put("source", source.getUUID());
			json.put("target", target.getUUID());
		} catch (UUIDNotAssignedException ex) {
			ex.printStackTrace();
		}

		UUIDTable.addToTable(this);
	}

	@Override
	protected void parseTemplate() {
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
		try {
			json.put("source", o.getUUID());
		} catch (UUIDNotAssignedException ex) {
			ex.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void setTarget(GameObject o) {
		try {
			json.put("target", o.getUUID());
		} catch (UUIDNotAssignedException ex) {
			ex.printStackTrace();
		}
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
