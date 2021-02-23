package dnd.tasks;

import java.util.LinkedList;

import dnd.events.eventgroups.EventGroup;
import gameobjects.entities.Entity;

/**
 * An abstract class which represents the ability of an Entity to do something,
 * as distinct from doing that activity
 * 
 * @author calvi
 *
 */
public abstract class Task {
	private String name;
	private LinkedList<EventGroup> eventGroups;
	// TODO: action economy cost

	/**
	 * Constructor for class Task
	 * 
	 * @param name ({@code String}) the name of the Task
	 */
	public Task(String name) {
		this.name = name;
		eventGroups = new LinkedList<EventGroup>();
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
		// TODO: return false if insufficient action economy
		// TODO: expend action economy
		System.out.println(invoker + " invokes Task " + this);
		for (EventGroup group : eventGroups) {
			invoker.queueEventGroup(group);
		}
		return true;
	}

	@Override
	public String toString() {
		return name;
	}
}
