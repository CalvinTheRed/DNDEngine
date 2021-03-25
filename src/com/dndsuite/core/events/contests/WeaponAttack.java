package com.dndsuite.core.events.contests;

import com.dndsuite.core.Item;
import com.dndsuite.core.events.Damage;
import com.dndsuite.core.gameobjects.Entity;
import com.dndsuite.dnd.combat.DamageDiceGroup;
import com.dndsuite.dnd.data.DamageType;
import com.dndsuite.maths.dice.Die;

public class WeaponAttack extends AttackRoll {
	public static final String EVENT_TAG_ID = "Weapon Attack";

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
		addTag(EVENT_TAG_ID);
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
