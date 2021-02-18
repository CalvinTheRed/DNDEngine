package dnd.events.dicecontests.savingthrow;

import dnd.data.EventShape;
import dnd.effects.Effect;
import dnd.items.Item;
import engine.patterns.Observer;
import engine.patterns.Subject;
import gameobjects.entities.Entity;

public class SpellSavingThrow extends SavingThrow implements Observer {
	protected Item implement;
	protected int level;

	public SpellSavingThrow(String name, EventShape shape, double[] range, double radius, int dcAbility,
			int saveAbility, int level) {
		super(name, shape, range, radius, dcAbility, saveAbility);
		this.level = level;
	}

	@Override
	public SpellSavingThrow clone() {
		SpellSavingThrow clone = new SpellSavingThrow(name, shape, range, radius, dcAbility, saveAbility, level);
		clone.advantage = advantage;
		clone.disadvantage = disadvantage;
		clone.bonus = bonus;
		for (Effect e : appliedEffects) {
			clone.appliedEffects.add(e);
		}
		clone.d20 = d20.clone();
		return clone;
	}

	@Override
	public void update(Subject s) {
		// TODO: figure out how to embed Lua in a later branch

	}

	@Override
	protected void invokeFallout(Entity source) {
	}

}
