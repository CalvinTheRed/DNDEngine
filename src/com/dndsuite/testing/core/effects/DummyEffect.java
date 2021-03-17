package com.dndsuite.testing.core.effects;

import com.dndsuite.core.effects.Effect;
import com.dndsuite.core.events.Event;
import com.dndsuite.core.gameobjects.Entity;

public class DummyEffect extends Effect {

	public DummyEffect(Entity source, Entity target) {
		super("", source, target);
	}

	@Override
	public boolean processEvent(Event e, Entity source, Entity target) {
		return false;
	}

}
