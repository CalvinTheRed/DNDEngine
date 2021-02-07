package dnd.effects;

import dnd.events.Event;
import dnd.events.dicecontest.AttackRoll;
import gameobjects.entities.Entity;

public class Dodge extends Effect {

	public Dodge(Entity source, Entity target) {
		super(source, target, "Dodge");
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean processEvent(Event e, Entity target) {
		if (e.isEffectApplied(this)) {
			return false;
		}
		if (e instanceof AttackRoll && target == getTarget()) {
			((AttackRoll)e).grantDisadvantage(this);
			return true;
		}
		return false;
	}

}
