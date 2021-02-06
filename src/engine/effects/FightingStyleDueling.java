package engine.effects;

import dnd.combat.DamageDiceGroup;
import engine.events.Damage;
import engine.events.Event;
import engine.events.attackrolls.WeaponAttack;
import gameobjects.entities.Entity;

public class FightingStyleDueling extends Effect {
	
	public FightingStyleDueling(Entity source, Entity target) {
		super(source, target, "Fighting Style (Dueling)");
	}
	
	@Override
	public boolean processEvent(Event e, Entity target) {
		if (isEnded()) {
			return false;
		}
		if (e instanceof Damage) {
			Damage d = (Damage)e;
			if (
					d.getParent() instanceof WeaponAttack 
					&& d.getParent().getSource() == getTarget()
					&& (d.getParent().getSource().getInventory().offhand() == null || !(d.getParent().getSource().getInventory().offhand().isWeapon()))
					&& !d.isEffectApplied(this)
			) {
					/* Fighting Style (Dueling):
					 * When you are wielding a melee weapon in one hand
					 * and no other weapons, you gain a 2+ bonus to damage
					 * rolls with that weapon.
					 */
				for (DamageDiceGroup group : d.getDamageDice()) {
					if (WeaponAttack.WEAPON_DAMAGE_TYPES.contains(group.getDamageType())) {
						/* Grant a +2 damage bonus to the first DamageDiceGroup
						 * which deals weapon-typed damage.
						 */
						System.out.println("Applying " + this);
						e.applyEffect(this);
						group.setDamageBonus(group.getDamageBonus() + 2);
						return true;
					}
				}
			}
		}
		return false;
	}
	
}
