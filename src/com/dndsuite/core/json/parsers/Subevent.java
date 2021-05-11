package com.dndsuite.core.json.parsers;

import java.util.ArrayList;

import org.json.simple.JSONObject;

import com.dndsuite.core.effects.Effect;
import com.dndsuite.core.gameobjects.GameObject;
import com.dndsuite.exceptions.SubeventMismatchException;

public abstract class Subevent {
	protected ArrayList<Integer> appliedEffects;
	protected ArrayList<String> tags;

	public Subevent() {
		appliedEffects = new ArrayList<Integer>();
		tags = new ArrayList<String>();
	}

	public void applyEffect(Effect e) throws Exception {
		int uuid = e.getUUID();
		if (appliedEffects.contains(uuid)) { // TODO: do better checks for different instances of same effects
			throw new Exception();
		} else {
			appliedEffects.add(uuid);
		}
	}

	protected void presentToEffects(GameObject source, GameObject target) {
		while (source.processSubevent(this) | target.processSubevent(this)) {
		}
	}

	public void addTag(String tag) {
		tags.add(tag);
	}

	public boolean hasTag(String tag) {
		return tags.contains(tag);
	}

	@Override
	public abstract Subevent clone();

	public abstract void parse(JSONObject json, GameObject eventSource, GameObject eventTarget)
			throws SubeventMismatchException;

}
