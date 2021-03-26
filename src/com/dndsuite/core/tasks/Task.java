package com.dndsuite.core.tasks;

import java.util.LinkedList;

import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import com.dndsuite.core.Scriptable;
import com.dndsuite.core.events.groups.EventGroup;
import com.dndsuite.core.gameobjects.Entity;

public class Task extends Scriptable {
	protected LinkedList<EventGroup> eventGroups;
	// TODO: action economy cost

	public Task(String script) {
		super(script);
		eventGroups = new LinkedList<EventGroup>();
	}

	protected void addEventGroup(EventGroup group) {
		eventGroups.add(group);
	}

	public boolean invoke(Entity invoker) {
		System.out.print("[JAVA] " + invoker + " invokes Task " + this + " (cost: ");

		Varargs vargs = globals.get("getTaskCost").invoke();
		String cost = vargs.tojstring(1);

		System.out.println(cost + ")");

		// TODO: return false if insufficient action economy
		// TODO: expend action economy
		globals.set("invoker", CoerceJavaToLua.coerce(invoker));
		globals.get("invokeTask").invoke();

		return true;
	}

}
