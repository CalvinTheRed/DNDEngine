package dnd.effects;

import dnd.events.Event;
import dnd.events.dicecontests.attackroll.AttackRoll;
import dnd.events.dicecontests.savingthrow.SavingThrow;
import gameobjects.entities.Entity;

/**
 * A class dedicated to representing the condition of dodging. A dodging target
 * has advantage on all Dex saves, and all attack rolls made against the target
 * have disadvantage if the target can see the attacker. This Effect ends at the
 * beginning of the target's next turn.
 * 
 * @author calvi
 *
 */
public class Dodge extends Effect {

	/**
	 * Constructor for class Dodge. This Effect has high priority in an Entity's
	 * activeEffects list.
	 * 
	 * @param source (Entity) the source of the Dodge Event
	 * @param target (Entity) the Entity which is Dodging
	 */
	public Dodge(Entity source, Entity target) {
		super(source, target, "Dodge", HI_PRIORITY);
	}

	@Override
	public boolean processEvent(Event e, Entity source, Entity target) {
		if (e instanceof AttackRoll && target == getTarget()) {
			// TODO: the dodging entity must be able to see the source
			try {
				e.applyEffect(this);
				((AttackRoll) e).grantDisadvantage();
				return true;
			} catch (Exception ex) {
			}
		} else if (e instanceof SavingThrow && target == getTarget()
				&& ((SavingThrow) e).getSaveAbility() == Entity.DEX) {
			try {
				e.applyEffect(this);
				((SavingThrow) e).grantAdvantage();
				return true;
			} catch (Exception ex) {
			}
		}
		return false;
	}

}
