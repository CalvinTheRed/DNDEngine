package com.dndsuite.core.effects;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.dndsuite.core.gameobjects.GameObject;
import com.dndsuite.core.json.JSONLoader;
import com.dndsuite.core.json.parsers.Condition;
import com.dndsuite.core.json.parsers.Function;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.core.json.parsers.conditions.DealsDamageType;
import com.dndsuite.core.json.parsers.conditions.HasTag;
import com.dndsuite.core.json.parsers.conditions.IsCriticalHit;
import com.dndsuite.core.json.parsers.conditions.IsCriticalMiss;
import com.dndsuite.core.json.parsers.conditions.IsRollAbove;
import com.dndsuite.core.json.parsers.conditions.IsRollBelow;
import com.dndsuite.core.json.parsers.functions.GrantAdvantage;
import com.dndsuite.core.json.parsers.functions.GrantDisadvantage;
import com.dndsuite.exceptions.ConditionMismatchException;
import com.dndsuite.exceptions.FunctionMismatchException;
import com.dndsuite.exceptions.UUIDKeyMissingException;

public class Effect extends JSONLoader {

	public static final Map<String, Condition> CONDITION_MAP = new HashMap<String, Condition>() {

		/**
		 * 
		 */
		private static final long serialVersionUID = -4761488814133689338L;

		{
			put("deals_damage_type", new DealsDamageType());
			put("has_tag", new HasTag());
			put("is_critical_hit", new IsCriticalHit());
			put("is_critical_miss", new IsCriticalMiss());
			put("is_roll_above", new IsRollAbove());
			put("is_roll_below", new IsRollBelow());
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
	}

	@SuppressWarnings("unchecked")
	public Effect(String file, GameObject source, GameObject target) {
		super("effects/" + file);
		try {
			json.put("source", source.getUUID());
			json.put("target", target.getUUID());
		} catch (UUIDKeyMissingException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	protected void parseBasePattern() {
		// TODO Auto-generated method stub

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
				}
			}
		}
		return false;
	}

}
