package dnd.tasks;

import dnd.events.EventGroup;

public class Disengage extends Task {

	public Disengage() {
		super("Disengage");
		EventGroup group1 = new EventGroup();
		group1.addEvent(new dnd.events.Disengage());
		addEventGroup(group1);
	}

}
