package engine.events.attackrolls;

import java.util.Arrays;
import java.util.LinkedList;

import dnd.combat.DamageDiceGroup;
import dnd.data.DamageType;
import dnd.items.Item;
import engine.events.Damage;
import gameobjects.entities.Entity;

public class WeaponAttack extends AttackRoll {
	
	public static final LinkedList<DamageType> WEAPON_DAMAGE_TYPES = new LinkedList<DamageType>(Arrays.asList(
			DamageType.BLUDGEONING,
			DamageType.BLUDGEONING_MAGICAL,
			DamageType.BLUDGEONING_SILVERED,
			DamageType.BLUDGEONING_MAGICAL_SILVERED,
			DamageType.PIERCING,
			DamageType.PIERCING_MAGICAL,
			DamageType.PIERCING_SILVERED,
			DamageType.PIERCING_MAGICAL_SILVERED,
			DamageType.SLASHING,
			DamageType.SLASHING_MAGICAL,
			DamageType.SLASHING_SILVERED,
			DamageType.SLASHING_MAGICAL_SILVERED
	));
	
	/* This class is designed to represent attack rolls made with
	 * explicit weapons such as a warhammer or dagger. This class
	 * is not intended to represent intrinsic attacks such as a
	 * slam attack or a bite attack, nor is this class intended
	 * to represent any form of spell attack.
	 */
	public WeaponAttack(Entity source, Item medium) {
		super(source, medium, "Weapon Attack", Entity.STR, false);
	}

	@Override
	protected void applyHit(Entity target) {
		Damage d = new Damage(getSource(), this);
		LinkedList<DamageDiceGroup> damageDice = new LinkedList<DamageDiceGroup>();
		if (getSource().getInventory().mainhand() == medium) {
			/* Main-hand weapon attacks gain a damage bonus equal to
			 * the ability modifier used to make the attack roll
			 */
			DamageDiceGroup weaponDamage;
			if (getSource().getInventory().mainhand() == getSource().getInventory().offhand()) {
				weaponDamage = getSource().getInventory().mainhand().getDamageDiceVersatile();
			}
			else {
				weaponDamage = getSource().getInventory().mainhand().getDamageDice();
			}
			
			int damageBonus = weaponDamage.getDamageBonus();
			damageBonus += getSource().getAbilityModifier(getAttackAbilityScore());
			weaponDamage.setDamageBonus(damageBonus);
			damageDice.add(weaponDamage);
			d.setDamageDice(damageDice);
		}
		else {
			/* Off-hand weapon attacks typically do not gain any
			 * additional damage bonuses, though certain effects
			 * can add them to the event.
			 */
			DamageDiceGroup weaponDamage = getSource().getInventory().mainhand().getDamageDice();
			damageDice.add(weaponDamage);
			d.setDamageDice(damageDice);
		}
		d.invoke(null);
		
		while (getSource().processEvent(d, target) || target.processEvent(d, target)) {
			/* Allows the effects applied to the source and the target to
			 * modify the parameters of the damage event (e.g. the Dueling
			 * fighting style grants a +2 damage bonus to single-handed
			 * weapon attacks if you are not holding another weapon in
			 * your second hand, while the Heavy Armor Master feat reduces
			 * all non-magical weapon damage by 3).
			 * 
			 * NOTE: all resistances/vulnerabilities/immunities are to be
			 * accounted for in the processDamageEvent() method, not in the
			 * processEvent() method.
			 */
		}
		
		target.processDamageEvent(d);
	}
	
	@Override
	protected void applyMiss(Entity target) {
		// No effects on a miss!
	}
	
}
