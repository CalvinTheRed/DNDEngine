package core.events;

import core.gameobjects.Entity;
import maths.dice.Die;

public abstract class DiceContest extends Event {
	public static final String EVENT_TAG_ID = "Dice Contest";

	protected Die d20;
	protected int bonus;

	public DiceContest(String script) {
		super(script);
		d20 = new Die(20);
		addTag(DiceContest.EVENT_TAG_ID);
	}

	public void addBonus(int bonus) {
		this.bonus += bonus;
	}

	public int getBonus() {
		return bonus;
	}

	public int getRawRoll() {
		return d20.getRoll();
	}

	public int getRoll() {
		return d20.getRoll() + bonus;
	}

	protected void roll() {
		d20.roll();
		boolean hasAdvantage = hasTag("Advantage");
		boolean hasDisadvantage = hasTag("Disadvantage");
		if (hasAdvantage && !hasDisadvantage) {
			System.out.println("[JAVA] Rolling with advantage!");
			int roll = d20.getRoll();
			d20.roll();
			if (d20.getRoll() > roll) {
				roll = d20.getRoll();
			}
		} else if (!hasAdvantage && hasDisadvantage) {
			System.out.println("[JAVA] Rolling with disadvantage!");
			int roll = d20.getRoll();
			d20.roll();
			if (d20.getRoll() < roll) {
				roll = d20.getRoll();
			}
		} else if (!hasAdvantage && !hasDisadvantage) {
			System.out.println("[JAVA] Rolling normally!");
		} else {
			System.out.println("[JAVA] Rolling with both advantage and disadvantage!");
		}
	}

	protected abstract void invokeFallout(Entity source);

}
