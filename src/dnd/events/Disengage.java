package dnd.events;

import dnd.data.EventShape;
import gameobjects.entities.Entity;
import maths.Vector;

public class Disengage extends Event {

	public Disengage() {
		super("Disengage");
		setRange(0,0);
		setShape(EventShape.SINGLE_TARGET);
		setRadius(0);
	}

	@Override
	public void invoke(Entity source, Vector targetPos) {
		super.invoke(source, targetPos);
		source.observeEffect(new dnd.effects.Disengage(source, source));
	}

}
