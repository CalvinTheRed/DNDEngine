package testing.dnd.effects;

import core.effects.Effect;
import core.events.Event;
import core.gameobjects.Entity;

public class DummyEffect extends Effect {

	public DummyEffect(Entity source, Entity target) {
		super("", source, target);
	}

	@Override
	public boolean processEvent(Event e, Entity source, Entity target) {
		return false;
	}

}
