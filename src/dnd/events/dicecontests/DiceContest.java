package dnd.events.dicecontests;

import dnd.events.Event;
import gameobjects.entities.Entity;
import maths.dice.Die;

/**
 * A class representing all instances of a d20 being rolled to resolve effects
 * such as attack rolls, saving throws, ability checks, etc.
 * 
 * @author calvi
 *
 */
public abstract class DiceContest extends Event {

	public static final int DISADVANTAGE = -1;
	public static final int NORMAL_ROLL = 0;
	public static final int ADVANTAGE = 1;
	public static final int CANCELLED_OUT = 2;

	protected int advantage;
	protected int disadvantage;
	protected int bonus;
	protected Die d20;

	/**
	 * Constructor for class DiceContest
	 * 
	 * @param name ({@code String}) the name of the DiceContest
	 */
	public DiceContest(String name) {
		super(name, name, Event.SINGLE_TARGET);
		d20 = new Die(20);
	}

	/**
	 * This function increments the advantage counter by 1, indicating that this die
	 * roll has advantage somehow.
	 */
	public void grantAdvantage() {
		advantage++;
	}

	/**
	 * This function increments the disadvantage counter by 1, indicating that this
	 * die roll has disadvantage somehow.
	 */
	public void grantDisadvantage() {
		disadvantage++;
	}

	/**
	 * This function returns whether this die roll has advantage, disadvantage,
	 * both, or neither.
	 * 
	 * @return {@code int} advantage state id [-1,2]
	 */
	private int getAdvantageState() {
		if (advantage > 0 && disadvantage > 0) {
			return CANCELLED_OUT;
		}
		if (advantage > 0) {
			return ADVANTAGE;
		}
		if (disadvantage > 0) {
			return DISADVANTAGE;
		}
		return NORMAL_ROLL;
	}

	/**
	 * This function adds a given bonus to the contained bonus. It does not
	 * overwrite the contained bonus.
	 * 
	 * @param bonus ({@code int}) a bonus to be added to the contained bonus
	 */
	public void addBonus(int bonus) {
		this.bonus += bonus;
	}

	/**
	 * This function returns the contained bonus.
	 * 
	 * @return {@code int} bonus
	 */
	public int getBonus() {
		return bonus;
	}

	/**
	 * This function returns the sum of the rolled d20 and the contained bonus. This
	 * function requires that roll() is called before it can return meaningful data.
	 * 
	 * @return {@code int} raw d20 rolled value before any bonuses are added to it
	 */
	public int getRawRoll() {
		return d20.getRoll();
	}

	/**
	 * This function rolls the d20, accounting for advantage and disadvantage.
	 */
	protected void roll() {
		d20.roll();
		int advantageState = getAdvantageState();
		if (advantageState == ADVANTAGE) {
			System.out.println("[JAVA] Rolling with advantage!");
			int roll = d20.getRoll();
			d20.roll();
			if (d20.getRoll() > roll) {
				roll = d20.getRoll();
			}
		} else if (advantageState == DISADVANTAGE) {
			System.out.println("[JAVA] Rolling with disadvantage!");
			int roll = d20.getRoll();
			d20.roll();
			if (d20.getRoll() < roll) {
				roll = d20.getRoll();
			}
		} else if (advantageState == NORMAL_ROLL) {
			System.out.println("[JAVA] Rolling normally!");
		} else {
			System.out.println("[JAVA] Rolling with both advantage and disadvantage!");
		}
	}

	/**
	 * This function causes the consequences of the DiceContest object's results to
	 * occur (e.g. causing Damage, applying an Effect, etc).
	 * 
	 * @param source ({@code Entity}) the Entity which caused this DiceContest to
	 *               occur
	 */
	protected abstract void invokeFallout(Entity source);

}
