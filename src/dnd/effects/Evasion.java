package dnd.effects;

import dnd.combat.DamageDiceGroup;
import dnd.events.Damage;
import dnd.events.Event;
import dnd.events.dicecontests.savingthrow.SavingThrow;
import gameobjects.entities.Entity;

public class Evasion extends Effect {

	public Evasion(Entity source, Entity target) {
		super(source, target, "Evasive", LO_PRIORITY);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean processEvent(Event e, Entity source, Entity target) {
		if (e instanceof Damage && target == getTarget()) {
			Event parent = ((Damage)e).getParent();
			if (parent instanceof SavingThrow && ((SavingThrow)parent).getSaveAbility() == Entity.DEX) {
				try {
					e.applyEffect(this);
					if (((SavingThrow)parent).getPassedTargets().contains(target)){
						// A creature with Evasion takes no damage on a successful Dex save
						((Damage)e).getDamageDice().clear();
					}
					else if (((SavingThrow)parent).getFailedTargets().contains(target)) {
						// A creature with Evasion takes half damage (rounded down) on an unsuccessful Dex save
						for (DamageDiceGroup group : ((Damage)e).getDamageDice()) {
							group.addDamageBonus(-(group.getSum() + 1) / 2);
						}
					}
					return true;
				} catch (Exception ex) {}
			}
		}
		return false;
	}

}
