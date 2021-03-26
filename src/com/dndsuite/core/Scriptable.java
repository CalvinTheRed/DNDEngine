package com.dndsuite.core;

import java.util.LinkedList;

import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

public abstract class Scriptable {
	protected Globals globals;
	protected String script;
	protected String name;
	protected LinkedList<String> tags;

	public Scriptable(String script) {
		this.script = script;
		tags = new LinkedList<String>();
		setName("Scriptable");
		if (script != null) {
			globals = JsePlatform.standardGlobals();
			globals.loadfile(script).call();
			globals.set("self", CoerceJavaToLua.coerce(this));
			globals.get("define").invoke();
		}
	}

	public void addTag(String tag) {
		tags.add(tag);
	}

	public boolean hasTag(String tag) {
		return tags.contains(tag);
	}

	public LinkedList<String> getTags() {
		return tags;
	}

	public void printTags() {
		for (String tag : tags) {
			System.out.println(tag);
		}
	}

	@Override
	public String toString() {
		return name;
	}

	/*
	 * -----------------------------------------------------------------------------
	 * Functions used from Lua define function -------------------------------------
	 * -----------------------------------------------------------------------------
	 */

	public void setName(String name) {
		this.name = name;
	}

}
