package engine.effects;

import engine.Manager;
import engine.events.Event;
import engine.events.attackrolls.AttackRoll;
import gameobjects.entities.Entity;

public class ViciousMockeryEffect extends Effect {

	public ViciousMockeryEffect(Entity source, Entity target) {
		super(source, target, "Vicious Mockery");
	}

	@Override
	public boolean processEvent(Event e, Entity target) {
		if (e.getSource() == this.target && e instanceof AttackRoll) {
			if (!((AttackRoll)e).isEffectApplied(this)) {
				((AttackRoll)e).applyEffect(this);
				((AttackRoll)e).grantDisadvantage();
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
