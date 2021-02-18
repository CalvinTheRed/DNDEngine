package testing.dnd.effects;

import dnd.effects.Effect;
import dnd.events.Event;
import gameobjects.entities.Entity;

public class DummyEffect extends Effect {

	public DummyEffect(Entity source, Entity target, String name, boolean sequencingPriority) {
		super(source, target, name, sequencingPriority);
	}

	@Override
	public boolean processEvent(Event e, Entity source, Entity target) {
		return false;
	}

}
