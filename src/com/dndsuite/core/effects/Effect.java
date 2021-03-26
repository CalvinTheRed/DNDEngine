package com.dndsuite.core.effects;

import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import com.dndsuite.core.Scriptable;
import com.dndsuite.core.events.Event;
import com.dndsuite.core.gameobjects.Entity;

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

	public boolean processEvent(Event e, Entity source, Entity target) {
		globals.set("event", CoerceJavaToLua.coerce(e));
		globals.set("source", CoerceJavaToLua.coerce(source));
		globals.set("target", CoerceJavaToLua.coerce(target));
		globals.get("processEventSafe").invoke();
		return false;
	}

	// TODO: add processTask() abstract function?

}
