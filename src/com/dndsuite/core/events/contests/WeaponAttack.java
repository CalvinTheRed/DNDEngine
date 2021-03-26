package com.dndsuite.core.events.contests;

import com.dndsuite.core.Item;
import com.dndsuite.core.events.DamageCalculation;
import com.dndsuite.core.gameobjects.Entity;
import com.dndsuite.dnd.combat.DamageDiceGroup;
import com.dndsuite.dnd.data.DamageType;
import com.dndsuite.maths.dice.Die;

public class WeaponAttack extends AttackRoll {
	protected Item medium;
	protected boolean mainhand;

	public WeaponAttack(Item medium, int attackAbility, String attackType, boolean mainhand) {
		super(null, attackAbility);
		this.medium = medium;
		this.attackType = attackType;
		this.mainhand = mainhand;
		// TODO: check this vv
		setRange(medium == null ? 5.0 : medium.getReach(), medium == null ? 5.0 : medium.getReach());
		setName(attackType + " Attack (" + (medium == null ? "Fist" : medium.toString()) + ", "
				+ Entity.getAbility(attackAbility) + ")");
		addTag(WeaponAttack.getEventID());
	}

	public Item getMedium() {
		return medium;
	}

	@Override
	protected void invokeFallout(Entity source) {
		DamageCalculation d = new DamageCalculation(this);
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

	public static String getEventID() {
		return "Weapon Attack";
	}

}
