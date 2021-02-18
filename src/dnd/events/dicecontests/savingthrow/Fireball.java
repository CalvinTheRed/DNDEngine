package dnd.events.dicecontests.savingthrow;

import dnd.combat.DamageDiceGroup;
import dnd.data.DamageType;
import dnd.data.EventShape;
import dnd.events.Damage;
import gameobjects.entities.Entity;

public class Fireball extends SavingThrow {
	protected int level;

	public Fireball(int dcAbility, int level) {
		super("Fireball", EventShape.SPHERE, new double[] {150.0, 150.0}, 20.0, dcAbility, Entity.DEX);
		this.level = level;
	}
	
	@Override
	public void invokeFallout(Entity source) {
		int dieSize = 6;
		int numDice = 5 + level;
		
		Damage d = new Damage(name, this);
		d.addDamageDiceGroup(new DamageDiceGroup(numDice, dieSize, DamageType.FIRE));
		d.invoke(source, null);
		
		// Targets who fail their Dex save take full damage
		for (Entity target : failedTargets) {
			d.invokeClone(source, target);
		}
		
		// Targets who pass their Dex save take half damage
		for (DamageDiceGroup group : d.getDamageDice()) {
			group.addDamageBonus(-(group.getSum() + 1) / 2);
		}
		for (Entity target : passedTargets) {
			d.invokeClone(source, target);
		}
	}

}
