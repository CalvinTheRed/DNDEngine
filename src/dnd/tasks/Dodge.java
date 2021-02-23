package dnd.tasks;

import dnd.events.eventgroups.EventGroup;

/**
 * A class which represents the ability of an Entity to dodge
 * 
 * @author calvi
 *
 */
public class Dodge extends Task {

	/**
	 * Constructor for class Dodge
	 */
	public Dodge() {
		super("Dodge");
		EventGroup group1 = new EventGroup();
		group1.addEvent(new dnd.events.Dodge());
		addEventGroup(group1);
	}

}
