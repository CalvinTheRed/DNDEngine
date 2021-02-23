package maths.dice;

import java.util.LinkedList;

/**
 * This class represents a collection of dice. The dice need not all be of the
 * same size
 * 
 * @author calvi
 *
 */
public class DiceGroup {
	protected LinkedList<Die> dice;
	protected int bonus;

	/**
	 * Constructor for class DiceGroup.
	 * 
	 * @note while the constructor only adds dice of one size, dice of different
	 *       sizes may be added after construction
	 * 
	 * @param numDice ({@code int}) the number of dice in the DiceGroup
	 * @param dieSize ({@code int}) thesize of the dice in the DiceGroup
	 */
	public DiceGroup(int numDice, int dieSize) {
		dice = new LinkedList<Die>();
		for (int i = 0; i < numDice; i++) {
			dice.add(new Die(dieSize));
		}
		bonus = 0;
	}

	/**
	 * This function adds a new Die to the DiceGroup
	 * 
	 * @param d ({@code Die}) the Die to be added
	 */
	public void addDie(Die d) {
		dice.add(d);
	}

	/**
	 * This function returns a list of all the dice in the DiceGroup
	 * 
	 * @return {@code LinkedList<Die>} dice
	 */
	public LinkedList<Die> getDice() {
		return dice;
	}

	/**
	 * This function adds to the bonus given to the dice rolled in this DiceGroup.
	 * It does not override the value of bonus, but rather adds to it
	 * 
	 * @param bonus {@code int} the value to be added to the current bonus
	 */
	public void addBonus(int bonus) {
		this.bonus += bonus;
	}

	/**
	 * This function returns the value of the bonus for this DiceGroup
	 * 
	 * @return {@code int} bonus
	 */
	public int getBonus() {
		return bonus;
	}

	/**
	 * This function rolls all of the diec in the DiceGroup
	 */
	public void roll() {
		for (Die d : dice) {
			d.roll();
		}
	}

	/**
	 * This function returns the sum of all the dice in the DiceGroup plus the value
	 * of the bonus. This function requires roll() to be called before it can return
	 * meaningful data
	 * 
	 * @return {@code int} sum
	 */
	public int getSum() {
		int sum = 0;
		for (Die d : dice) {
			sum += d.getRoll();
		}
		return sum + bonus;
	}

}
