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
		for (Entity target : targets) {
			d.invokeClone(source, target);
		}
	}

}
