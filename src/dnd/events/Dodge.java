package dnd.events;

import dnd.data.EventShape;
import gameobjects.entities.Entity;
import maths.Vector;

/**
 * This class represents the act of beginning to dodge, as distinct from the
 * ability to dodge or the status of dodging
 * 
 * @author calvi
 *
 */
public class Dodge extends Event {

	/**
	 * Constructor for class Dodge
	 */
	public Dodge() {
		super("Dodge");
		setRange(0, 0);
		setShape(EventShape.SINGLE_TARGET);
		setRadius(0);
	}

	@Override
	public void invoke(Entity source, Vector targetPos) {
		super.invoke(source, targetPos);
		source.addEffect(new dnd.effects.Dodge(source, source));
	}

}
