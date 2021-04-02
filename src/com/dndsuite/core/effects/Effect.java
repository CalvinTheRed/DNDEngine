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

	public void processEvent(Event e, Entity source, GameObject target) {
		try {
			passToLua("event", e);
			passToLua("source", source);
			passToLua("target", target);
			invokeFromLua("processEventSafe");
		} catch (Exception ex) {
			processEventSafe(e, source, target);
		}
	}
	
	protected void processEventSafe(Event e, Entity source, GameObject target) {
		/*
		 * This function to be overridden by derived classes
		 * of Event. This function represents the invokeSafe()
		 * Lua script function, and acts as a gateway to the
		 * Effect logic.
		 */
	}

	// TODO: add processTask() function?

}
