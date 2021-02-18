package dnd.effects;

import dnd.combat.DamageDiceGroup;
import dnd.events.Damage;
import dnd.events.Event;
import dnd.events.dicecontests.savingthrow.SavingThrow;
import gameobjects.entities.Entity;

/**
 * A class representing the Evasion feature. An Entity with Evasion takes half
 * damage from Dex-based saving throw Events if it fails its save, and no damage
 * if it passes its save. This Effect, once granted, typically never ends.
 * 
 * @author calvi
 *
 */
public class Evasion extends Effect {

	/**
	 * Constructor for class Evasion. This Effect has low priority in an Entity's
	 * activeEffects list.
	 * 
	 * @param source (Entity) the Entity responsible for creating this Effect
	 * @param target (Entity) the Entity which is inflicted with this Effect
	 */
	public Evasion(Entity source, Entity target) {
		super(source, target, "Evasive", LO_PRIORITY);
	}

	@Override
	public boolean processEvent(Event e, Entity source, Entity target) {
		if (e instanceof Damage && target == getTarget()) {
			Event parent = ((Damage) e).getParent();
			if (parent instanceof SavingThrow && ((SavingThrow) parent).getSaveAbility() == Entity.DEX) {
				try {
					e.applyEffect(this);
					if (((SavingThrow) parent).getPassedTargets().contains(target)) {
						((Damage) e).getDamageDice().clear();
					} else if (((SavingThrow) parent).getFailedTargets().contains(target)) {
						for (DamageDiceGroup group : ((Damage) e).getDamageDice()) {
							group.addDamageBonus(-(group.getSum() + 1) / 2);
						}
					}
					return true;
				} catch (Exception ex) {
				}
			}
		}
		return false;
	}

}
