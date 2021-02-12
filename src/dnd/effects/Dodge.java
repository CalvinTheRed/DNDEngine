package dnd.effects;

import dnd.events.Event;
import dnd.events.dicecontests.AttackRoll;
import gameobjects.entities.Entity;

public class Dodge extends Effect {

	public Dodge(Entity source, Entity target) {
		super(source, target, "Dodge");
	}

	@Override
	public boolean processEvent(Event e, Entity source, Entity target) {
		if (e instanceof AttackRoll && target == getTarget()) {
			try {
				((AttackRoll)e).grantDisadvantage(this);
				return true;
			} catch (Exception ex) {}
		}
		return false;
	}

}
