package dnd.events.dicecontests.savingthrow;

import dnd.combat.DamageDiceGroup;
import dnd.data.DamageType;
import dnd.data.EventShape;
import dnd.events.Damage;
import gameobjects.entities.Entity;

public class SacredFlame extends SpellSavingThrow {

	public SacredFlame(int dcAbility) {
		super("Sacred Flame", EventShape.SINGLE_TARGET, new double[] { 60.0, 60.0 }, 0, dcAbility, Entity.DEX, 0);
	}

	@Override
	public void invokeFallout(Entity source) {
		int dieSize = 8;
		int numDice;

		if (source.getLevel() < 5) {
			numDice = 1;
		} else if (source.getLevel() < 11) {
			numDice = 2;
		} else if (source.getLevel() < 17) {
			numDice = 3;
		} else {
			numDice = 4;
		}

		Damage d = new Damage(name, this);
		d.addDamageDiceGroup(new DamageDiceGroup(numDice, dieSize, DamageType.RADIANT));
		d.invoke(source, null);
		for (Entity target : targets) {
			d.invokeClone(source, target);
		}
	}

}
