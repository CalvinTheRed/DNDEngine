package testing.dnd.effects;

import core.Effect;
import core.events.Event;
import core.gameobjects.Entity;

public class DummyEffect extends Effect {

	public DummyEffect(Entity source, Entity target, String name) {
		super("", name, source, target);
	}

	@Override
	public boolean processEvent(Event e, Entity source, Entity target) {
		return false;
	}

}
