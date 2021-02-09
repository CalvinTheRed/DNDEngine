package dnd.tasks;

import dnd.events.eventgroups.EventGroup;

public class Dodge extends Task {

	public Dodge() {
		super("Dodge");
		EventGroup group1 = new EventGroup();
		group1.addEvent(new dnd.events.Dodge());
		addEventGroup(group1);
	}

}
