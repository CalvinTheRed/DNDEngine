package com.dndsuite.core.tasks;

import java.util.LinkedList;

import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import com.dndsuite.core.Scriptable;
import com.dndsuite.core.events.groups.EventGroup;
import com.dndsuite.core.gameobjects.Entity;

/**
 * An abstract class which represents the ability of an Entity to do something,
 * as distinct from doing that activity
 * 
 * @author calvi
 *
 */
public class Task extends Scriptable {
	protected String name;
	protected LinkedList<EventGroup> eventGroups;
	// TODO: action economy cost

	/**
	 * Constructor for class Task
	 * 
	 * @param name ({@code String}) the name of the Task
	 */
	public Task(String script) {
		super(script);
		eventGroups = new LinkedList<EventGroup>();
		if (globals != null) {
			globals.set("task", CoerceJavaToLua.coerce(this));
			globals.get("define").invoke();
		}
	}

	/**
	 * This function adds an EventGroup to the contained list of EventGroups which
	 * are generated which this Task is invoked
	 * 
	 * @param group ({@code EventGroup}) the EventGroup to be added
	 */
	protected void addEventGroup(EventGroup group) {
		eventGroups.add(group);
	}

	/**
	 * This function represents exchanging some action economy cost for access to
	 * the EventGroups which are contained in this Task. Every Task object will have
	 * a different set of EventGroups which it can generate if invoked
	 * 
	 * @param invoker ({@code Entity}) the Entity invoking this Task
	 * @return {@code boolean} whether the invoker has successfully invoked this
	 *         Task
	 */
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

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
