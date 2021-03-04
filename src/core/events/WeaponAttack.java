package core.events;

import core.Item;
import core.gameobjects.Entity;
import dnd.combat.DamageDiceGroup;
import dnd.data.DamageType;

public class WeaponAttack extends AttackRoll {
	protected Item medium;
	protected String attackType;
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

	@Override
	protected void invokeFallout(Entity source) {
		Damage d = new Damage(this);
		DamageDiceGroup damageDice;
		if (medium == null) {
			damageDice = new DamageDiceGroup(1, 4, DamageType.BLUDGEONING);
		} else {
			damageDice = medium.getDamageDice();
		}
		if (mainhand) {
			damageDice.addBonus(source.getAbilityModifier(attackAbility));
		}
		d.addDamageDiceGroup(damageDice);
		d.invoke(source, null);
		d.clone().invokeAsClone(source, target);
	}

}
