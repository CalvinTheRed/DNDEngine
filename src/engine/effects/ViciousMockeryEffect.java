package engine.effects;

import engine.events.Event;
import engine.events.attackrolls.AttackRoll;
import gameobjects.entities.Entity;

public class ViciousMockeryEffect extends Effect {

	public ViciousMockeryEffect(Entity source, Entity target) {
		super(source, target, "Vicious Mockery");
	}

	@Override
	public boolean processEvent(Event e, Entity target) {
		if (isEnded()) {
			return false;
		}
		if (e.getSource() == getTarget() && e instanceof AttackRoll && !((AttackRoll)e).isEffectApplied(this)) {
			System.out.println("Applying " + this);
			((AttackRoll)e).applyEffect(this);
			((AttackRoll)e).grantDisadvantage();
			end();
			return true;
		}
		return false;
	}
	
}
