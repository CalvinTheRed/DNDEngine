package dnd.effects;

import dnd.events.Event;
import gameobjects.entities.Entity;

public class Disengage extends Effect {

	public Disengage(Entity source, Entity target) {
		super(source, target, "Disengage");
	}

	@Override
	public boolean processEvent(Event e, Entity target) {
		/* It is assumed that opportunity attacks will search for the
		 * presence of the Effect prior to offering the chance to be
		 * invoked. The Disengage Effect does not do anything itself.
		 */
		return false;
	}

}
