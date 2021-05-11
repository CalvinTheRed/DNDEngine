package com.dndsuite.core.json.parsers.subevents;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.dndsuite.core.UUIDTable;
import com.dndsuite.core.effects.Effect;
import com.dndsuite.core.gameobjects.GameObject;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.exceptions.SubeventMismatchException;
import com.dndsuite.exceptions.UUIDKeyMissingException;

public class ApplyEffect extends Subevent {

	@Override
	@SuppressWarnings("unchecked")
	public void parse(JSONObject json, GameObject eventSource, GameObject eventTarget)
			throws SubeventMismatchException {
		String subevent = (String) json.get("subevent");
		if (!subevent.equals("apply_effect")) {
			throw new SubeventMismatchException("apply_effect", subevent);
		}

		String effectName = (String) json.get("effect");
		GameObject subeventTarget;
		String subeventTargetAlias = (String) json.get("apply_to");
		if (subeventTargetAlias.equals("source")) {
			subeventTarget = eventSource;
		} else if (subeventTargetAlias.equals("target")) {
			subeventTarget = eventTarget;
		} else {
			System.out.println("[ApplyEffect] invalid apply_to value detected: " + subeventTargetAlias);
			return;
		}

		presentToEffects(eventSource, subeventTarget);
		Effect e = new Effect(effectName, eventSource, subeventTarget);
		UUIDTable.addToTable(e);
		JSONArray effects = (JSONArray) subeventTarget.getJSONData().remove("effects");
		try {
			effects.add(e.getUUID());
		} catch (UUIDKeyMissingException ex) {
			ex.printStackTrace();
		}
		subeventTarget.getJSONData().put("effects", effects);
	}

	@Override
	public String toString() {
		return "Test Subevent";
	}

	@Override
	public ApplyEffect clone() {
		ApplyEffect clone = new ApplyEffect();
		return clone;
	}

}
