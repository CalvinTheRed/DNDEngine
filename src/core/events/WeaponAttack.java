package core.events;

import core.Item;
import core.gameobjects.Entity;
import dnd.combat.DamageDiceGroup;
import dnd.data.DamageType;
import maths.dice.Die;

public class WeaponAttack extends AttackRoll {
	protected Item medium;
	protected boolean mainhand;

	public WeaponAttack(Item medium, int attackAbility, String attackType, boolean mainhand) {
		super(null, attackAbility);
		this.medium = medium;
		this.attackType = attackType;
		// TODO: check this vv
		setRange(medium == null ? 5.0 : medium.getReach(), medium == null ? 5.0 : medium.getReach());
		this.mainhand = mainhand;
		name = attackType + " Attack (" + (medium == null ? "Fist" : medium.toString()) + ", "
				+ Entity.getAbility(attackAbility) + ")";
	}

	public Item getMedium() {
		return medium;
	}

	@Override
	protected void invokeFallout(Entity source) {
		Damage d = new Damage(this);
		DamageDiceGroup damageDice;

		if (getRawRoll() == Die.CRITICAL_HIT) {
			d.addTag(AttackRoll.CRITICAL_HIT);
		}

		if (medium == null) {
			damageDice = new DamageDiceGroup(1, 4, DamageType.BLUDGEONING);
		} else {
			damageDice = medium.getDamageDice(attackType);
			if (source.getMainhand() == source.getOffhand() && source.getMainhand().hasTag(Item.VERSATILE)) {
				damageDice.makeVersatile();
			}		
		}
		if (mainhand) {
			damageDice.addBonus(source.getAbilityModifier(attackAbility));
		}

		d.addDamageDiceGroup(damageDice);
		d.invoke(source, null);
		d.clone().invokeAsClone(source, target);
	}

}
