package com.dndsuite.core.json.parsers;

import java.util.ArrayList;

import org.json.simple.JSONObject;

import com.dndsuite.core.Effect;
import com.dndsuite.core.Event;
import com.dndsuite.core.GameObject;
import com.dndsuite.core.Taggable;
import com.dndsuite.core.UUIDTable;
import com.dndsuite.exceptions.JSONFormatException;
import com.dndsuite.exceptions.SubeventMismatchException;

public abstract class Subevent implements Taggable {
	protected ArrayList<Long> appliedEffects;
	protected ArrayList<String> tags;
	protected Event parent;

	public Subevent() {
		appliedEffects = new ArrayList<Long>();
		tags = new ArrayList<String>();
	}

	public void applyEffect(Effect e) throws Exception {
		String effectName = (String) e.getJSONData().get("name");
		for (long uuid : appliedEffects) {
			Effect tmp = (Effect) UUIDTable.get(uuid);
			// Throw exception if effect with the same name is already applied
			if (effectName.equals(tmp.getJSONData().get("name"))) {
				throw new Exception();
			}
		}
		appliedEffects.add(e.getUUID());
	}

	protected void presentToEffects(GameObject source, GameObject target) {
		while (source.processSubevent(this) | target.processSubevent(this))
			;
	}

	public void addTag(String tag) {
		tags.add(tag);
	}

	public void removeTag(String tag) {
		if (tags.contains(tag)) {
			tags.remove(tag);
		}
	}

	public boolean hasTag(String tag) {
		return tags.contains(tag);
	}

	public Event getParentEvent() {
		return parent;
	}

	public void setParentEvent(Event e) {
		parent = e;
	}

	@Override
	public abstract Subevent clone();

	public abstract void parse(JSONObject json, Event e, GameObject eSource, GameObject eTarget)
			throws SubeventMismatchException, JSONFormatException;

}
