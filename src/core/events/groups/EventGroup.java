package core.events.groups;

import java.util.LinkedList;

import core.events.Event;

/**
 * A class dedicated to representing actions which are waiting to be taken,
 * after their necessary resources have been spent. All Events contained herein,
 * if invoked, will remove the entire EventGroup worth of Events from an
 * Entity's event queue
 * 
 * @author calvi
 *
 */
public class EventGroup {
	protected LinkedList<Event> events;
	protected LinkedList<EventGroup> groups; // TODO: does this collection get used?

	/**
	 * Constructor for class DiceGroup
	 */
	public EventGroup() {
		events = new LinkedList<Event>();
		groups = new LinkedList<EventGroup>();
	}

	/**
	 * This function adds an Event to the list of contained Events
	 * 
	 * @param e ({@code Event}) the Event to be added to the contained list
	 */
	public void addEvent(Event e) {
		events.add(e);
	}

	/**
	 * </p>
	 * This function adds an EventGroup to the list of contained EventGroups. This
	 * is to be utilized if a Task constructs a nested hierarchy of OR-relational
	 * Events, any one of which is sufficient to consume the EventGroup
	 * <p>
	 * </p>
	 * Example: the Task BladesingerExtraAttack creates the following EventGroups:
	 * <p>
	 * 
	 * // TODO: learn formatting conventions and finish this javadoc
	 * 
	 * @param group
	 */
	public void addEventGroup(EventGroup group) {
		groups.add(group);
	}

	/**
	 * This function returns a list of all contained Events and all Events contained
	 * within the contained EventGroups
	 * 
	 * @return {@code LinkedList<Event>} all events contained within this object,
	 *         recursively
	 */
	public LinkedList<Event> getEvents() {
		LinkedList<Event> list = new LinkedList<Event>();
		list.addAll(events);
		for (EventGroup group : groups) {
			list.addAll(group.getEvents());
		}
		return list;
	}

	@Override
	public String toString() {
		String val = "[";
		for (int i = 0; i < events.size(); i++) {
			val += events.get(i);
			if (i < events.size() - 1) {
				val += ", ";
			}
		}
		for (EventGroup group : groups) {
			val += ", " + group;
		}
		return val + "]";
	}

}
