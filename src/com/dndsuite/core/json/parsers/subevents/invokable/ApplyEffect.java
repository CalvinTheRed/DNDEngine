package com.dndsuite.core.json.parsers.subevents.invokable;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.dndsuite.core.Effect;
import com.dndsuite.core.Event;
import com.dndsuite.core.GameObject;
import com.dndsuite.core.UUIDTable;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.exceptions.JSONFormatException;
import com.dndsuite.exceptions.SubeventMismatchException;

/**
 * ApplyEffect is a derivation of Subevent which can be invoked from within a
 * JSON file. This Subevent loads an Effect and applies it to the target.
 * 
 * @author Calvin Withun
 *
 */
public class ApplyEffect extends Subevent {

	@Override
	@SuppressWarnings("unchecked")
	public void parse(JSONObject json, Event e, GameObject eSource, GameObject eTarget)
			throws SubeventMismatchException, JSONFormatException {
		super.parse(json, e, eSource, eTarget);
		String subevent = (String) json.get("subevent");
		if (!subevent.equals("apply_effect")) {
			throw new SubeventMismatchException("apply_effect", subevent);
		}

		String effectName = (String) json.get("effect");
		GameObject subeventTarget;
		String subeventTargetAlias = (String) json.get("apply_to");
		if (subeventTargetAlias.equals("source")) {
			subeventTarget = eSource;
		} else if (subeventTargetAlias.equals("target")) {
			subeventTarget = eTarget;
		} else {
			// TODO: is this the best way to handle this situation?
			System.out.println("[ApplyEffect] invalid apply_to value detected: " + subeventTargetAlias);
			return;
		}

		presentToEffects(eSource, subeventTarget);
		Effect effect = new Effect(effectName, eSource, subeventTarget);
		UUIDTable.addToTable(effect);
		JSONArray effects = (JSONArray) subeventTarget.getJSONData().remove("effects");
		effects.add(effect.getUUID());
		subeventTarget.getJSONData().put("effects", effects);
	}

	@Override
	public String toString() {
		return "ApplyEffect Subevent";
	}

	@Override
	public ApplyEffect clone() {
		ApplyEffect clone = new ApplyEffect();
		clone.parent = getParentEvent();
		return clone;
	}

}
