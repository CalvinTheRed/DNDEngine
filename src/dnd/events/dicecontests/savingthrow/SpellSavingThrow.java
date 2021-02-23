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

	/**
	 * 
	 * @param name        ({@code String}) the name of the SpellSavingThrow
	 * @param shape       ({@code EventShape}) the shape of the SpellSavingThrow's
	 *                    area of effect
	 * @param range       ({@code double[2]}) the range of the SpellSavingThrow
	 * @param radius      ({@code double}) the radius of the SpellSavingThrow
	 * @param dcAbility   ({@code int}) the index of the ability which the source of
	 *                    the SpellSavingThrow will use to determine the spell save
	 *                    DC
	 * @param saveAbility ({@code int}) the index of the ability which the target of
	 *                    the SpellSavingThrow will use to contest the source's
	 *                    spell save DC
	 * @param level       ({@code int}) the level at which the spell was cast (this
	 *                    indicates player level in the case of a cantrip, rather
	 *                    than spell level)
	 */
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
