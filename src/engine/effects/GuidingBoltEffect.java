package engine.effects;

import engine.events.Event;
import engine.events.attackrolls.AttackRoll;
import gameobjects.entities.Entity;

public class GuidingBoltEffect extends Effect {

	public GuidingBoltEffect(Entity source, Entity target) {
		super(source, target, "Guiding Bolt");
	}

	@Override
	public boolean processEvent(Event e, Entity target) {
		if (isEnded()) {
			return false;
		}
		if (target == getTarget() && e instanceof AttackRoll && !((AttackRoll)e).isEffectApplied(this)) {
			System.out.println("Applying " + this);
			((AttackRoll)e).applyEffect(this);
			((AttackRoll)e).grantAdvantage();
			end();
			return true;
		}
		return false;
	}
	
}
