package com.dndsuite.core.effects;

import com.dndsuite.core.Scriptable;
import com.dndsuite.core.events.Event;
import com.dndsuite.core.gameobjects.Entity;
import com.dndsuite.core.gameobjects.GameObject;

public class Effect extends Scriptable {
	protected Entity source;
	protected Entity target;
	protected boolean ended;

	public Effect(String script, Entity source, Entity target) {
		super(script);
		this.source = source;
		this.target = target;
		ended = false;
	}

	public final Entity getSource() {
		return source;
	}

	public final Entity getTarget() {
		return target;
	}

	public final void end() {
		ended = true;
	}

	public final boolean isEnded() {
		return ended;
	}

	public boolean processEvent(Event e, GameObject source, GameObject target) {
		try {
			passToLua("event", e);
			passToLua("source", source);
			passToLua("target", target);
			invokeFromLua("processEventSafe");
		} catch (Exception ex) {
			
		}
		return false;
	}

	// TODO: add processTask() function?

}
