package com.dndsuite.core.tasks;

import java.util.LinkedList;

import org.luaj.vm2.Varargs;

import com.dndsuite.core.Scriptable;
import com.dndsuite.core.events.groups.EventGroup;
import com.dndsuite.core.gameobjects.Entity;

public class Task extends Scriptable {
	protected LinkedList<EventGroup> eventGroups;
	// TODO: action economy cost

	public Task(String script) {
		super(script);
	}

	protected void addEventGroup(EventGroup group) {
		eventGroups.add(group);
	}
	
	public String getCost() {
		try {
			Varargs va = invokeFromLua("cost");
			return va.tojstring(1);
		} catch (Exception e) {
			return "COST UNDEFINED";
		}
		
	}

	public boolean invoke(Entity invoker) {
		System.out.println("[JAVA] " + invoker + " invokes Task " + this + " (cost: " + getCost() + ")");

		// TODO: return false if insufficient action economy
		// TODO: expend action economy
		try {
			passToLua("invoker", invoker);
			invokeFromLua("invokeTask");
		} catch (Exception e) {
			for (EventGroup group : eventGroups) {
				invoker.queueEventGroup(group);
			}
		}
		
		return true;
	}
	
	@Override
	protected void setup() {
		super.setup();
		eventGroups = new LinkedList<EventGroup>();
	}

}
