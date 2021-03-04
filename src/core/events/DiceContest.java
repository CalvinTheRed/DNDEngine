package core.events;

import core.gameobjects.Entity;
import maths.dice.Die;

public abstract class DiceContest extends Event {
	protected Die d20;
	protected int bonus;

	public DiceContest(String script, String targetTag) {
		super(script, targetTag);
		d20 = new Die(20);
		addTag("Dice Contest");
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
