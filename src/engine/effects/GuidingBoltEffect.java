package engine.effects;

import engine.Manager;
import engine.events.Event;
import engine.events.attackrolls.AttackRoll;
import gameobjects.entities.Entity;

public class GuidingBoltEffect extends Effect {

	public GuidingBoltEffect(Entity source, Entity target) {
		super(source, target, "Guiding Bolt");
	}

	@Override
	public boolean processEvent(Event e, Entity target) {
		if (target == this.target && e instanceof AttackRoll) {
			if (!((AttackRoll)e).isEffectApplied(this)) {
				((AttackRoll)e).applyEffect(this);
				((AttackRoll)e).grantAdvantage();
				Manager.removeEffect(this);
				return true;
			}
		}
		return false;
	}

	@Override
	public void expire() {
		
	}

}
