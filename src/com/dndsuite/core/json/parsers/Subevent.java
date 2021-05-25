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

/**
 * Subevent is an abstract class which represents one piece of a larger Event.
 * Subevents work with other Subevents to orchestrate theemergent behavior of an
 * Event.
 * 
 * @author Calvin Withun
 *
 */
public abstract class Subevent implements Taggable {
	protected ArrayList<Long> appliedEffects;
	protected ArrayList<String> tags;
	protected Event parent;
	protected long target;

	public Subevent() {
		appliedEffects = new ArrayList<Long>();
		tags = new ArrayList<String>();
	}

	// TODO: can a damage bonus Effect be applied multiple times to a damage roll if
	// there are several targets? Consider allowing for Effects to be applied to
	// Events as well
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

	/**
	 * This function allows the Effects active on the source and the target to
	 * examine the Subevent and apply changes if appropriate.
	 * 
	 * @param source - the GameObject which is initiating this Subevent
	 * @param target - the GameObject at which this Subevent is being directed
	 */
	protected void presentToEffects(GameObject source, GameObject target) {
		while (source.processSubevent(this) | target.processSubevent(this))
			;
	}

	@Override
	public void addTag(String tag) {
		tags.add(tag);
	}

	@Override
	public void removeTag(String tag) {
		if (tags.contains(tag)) {
			tags.remove(tag);
		}
	}

	@Override
	public boolean hasTag(String tag) {
		return tags.contains(tag);
	}

	/**
	 * This function returns the Event which precipitated this Subevent.
	 * 
	 * @return - the parent Event
	 */
	public Event getParentEvent() {
		return parent;
	}

	/**
	 * This function allows for the modification of the Event which this Subevent is
	 * recognized to hail from. This function is currently only intended to be used
	 * in testing.
	 * 
	 * @param e - the new parent Event
	 */
	public void setParentEvent(Event e) {
		parent = e;
	}

	@Override
	public abstract Subevent clone();

	public void parse(JSONObject json, Event e, GameObject eSource, GameObject eTarget)
			throws SubeventMismatchException, JSONFormatException {
		parent = e;
		target = eTarget.getUUID();
		String subevent = (String) json.get("subevent");
		addTag(subevent);
	}

	public long getTarget() {
		return target;
	}

}
