package dnd.events;

import gameobjects.entities.Entity;
import maths.Vector;

public class Dodge extends Event {

	public Dodge() {
		super("Dodge");
	}

	@Override
	public void invoke(Entity source, Vector targetPos) {
		source.observeEffect(new dnd.effects.Dodge(source, source));
	}

}
