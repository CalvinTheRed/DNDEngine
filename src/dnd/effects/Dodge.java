package dnd.effects;

import dnd.combat.DamageDiceGroup;
import dnd.events.Damage;
import dnd.events.Event;
import dnd.events.dicecontests.attackroll.AttackRoll;
import dnd.events.dicecontests.savingthrow.SavingThrow;
import gameobjects.entities.Entity;

public class Dodge extends Effect {

	public Dodge(Entity source, Entity target) {
		super(source, target, "Dodge");
	}

	@Override
	public boolean processEvent(Event e, Entity source, Entity target) {
		if (e instanceof AttackRoll && target == getTarget()) {
			try {
				e.applyEffect(this);
				((AttackRoll)e).grantDisadvantage(this);
				return true;
			} catch (Exception ex) {}
		}
		else if (e instanceof SavingThrow && target == getTarget() && ((SavingThrow)e).getSaveAbility() == Entity.DEX) {
			try {
				e.applyEffect(this);
				((SavingThrow)e).grantAdvantage(this);
				return true;
			} catch (Exception ex) {}
		}
		// TODO: remove
		else if (e instanceof Damage && target == getTarget()) {
			for (DamageDiceGroup group : ((Damage)e).getDamageDice()) {
				try {
					e.applyEffect(this);
					group.grantResistance();
					return true;
				} catch (Exception ex) {}
			}
		}
		return false;
	}

}
