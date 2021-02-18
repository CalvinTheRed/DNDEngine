package dnd.events;

import dnd.data.EventShape;
import gameobjects.entities.Entity;
import maths.Vector;

public class Dodge extends Event {

	public Dodge() {
		super("Dodge");
		setRange(0,0);
		setShape(EventShape.SINGLE_TARGET);
		setRadius(0);
	}

	@Override
	public void invoke(Entity source, Vector targetPos) {
		super.invoke(source, targetPos);
		source.addEffect(new dnd.effects.Dodge(source, source));
	}

}
