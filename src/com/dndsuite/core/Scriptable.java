package com.dndsuite.core;

import java.util.LinkedList;

import org.luaj.vm2.Globals;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

public abstract class Scriptable {
	private Globals globals;
	
	protected String script;
	protected String name;
	protected LinkedList<String> tags;

	public Scriptable(String script) {
		setup();
		setName("Scriptable");
		try {
			this.script = script;
			globals = JsePlatform.standardGlobals();
			globals.loadfile(script).call();
			passToLua("self", this);
			invokeFromLua("define");
		} catch (Exception ex) {
		}
	}

	public void addTag(String tag) {
		tags.add(tag);
	}

	protected void cloneDataTo(Scriptable s) {
		s.tags.clear();
		s.tags.addAll(tags);
		s.globals = globals; // TODO: does cloning globals cause problems?
	}
	
	public Globals getGlobals() {
		return globals;
	}

	public LinkedList<String> getTags() {
		return tags;
	}

	public boolean hasTag(String tag) {
		return tags.contains(tag);
	}
	
	public Varargs invokeFromLua(String function) throws Exception {
		if (globals != null) {
			return globals.get(function).invoke();
		}
		throw new Exception("Scriptable has no globals");
	}
	
	public void passToLua(String varname, Object value) {
		if (globals != null) {
			globals.set(varname, CoerceJavaToLua.coerce(value));
		}
	}

	public void setName(String name) {
		this.name = name;
	}
	
	protected void setup() {
		tags = new LinkedList<String>();
	}

	@Override
	public String toString() {
		return name;
	}

}
