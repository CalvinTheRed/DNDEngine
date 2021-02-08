package dnd.effects;

import dnd.events.Event;
import dnd.events.dicecontest.AttackRoll;
import gameobjects.entities.Entity;

public class Dodge extends Effect {

	public Dodge(Entity source, Entity target) {
		super(source, target, "Dodge");
	}

	@Override
	public boolean processEvent(Event e, Entity target) {
		if (e instanceof AttackRoll && target == getTarget()) {
			try {
				((AttackRoll)e).grantDisadvantage(this);
				return true;
			} catch (Exception ex) {
				System.err.println("ERR: Effect " + e + " is already applied to Event " + this);
				ex.printStackTrace();
			}
		}
		return false;
	}

}
