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
		System.out.println("[JAVA] " + invoker + " invokes Task " + this + " (cost: " + getCost() + ")");

		// TODO: return false if insufficient action economy
		// TODO: expend action economy
		if (globals != null) {
			globals.set("invoker", CoerceJavaToLua.coerce(invoker));
			globals.get("invokeTask").invoke();
		} else {
			for (EventGroup group : eventGroups) {
				invoker.queueEventGroup(group);
			}
		}
		
		return true;
	}
	
	public String getCost() {
		Varargs vargs = globals.get("cost").invoke();
		return vargs.tojstring(1);
	}

}
