package com.dndsuite.maths.dice;

import java.util.ArrayList;

/**
 * This class represents a collection of dice. The dice need not all be of the
 * same size
 * 
 * @author calvi
 *
 */
public class DiceGroup {
	protected ArrayList<Die> dice;
	protected long bonus;

	public DiceGroup(long numDice, long dieSize) {
		dice = new ArrayList<Die>();
		for (int i = 0; i < numDice; i++) {
			dice.add(new Die(dieSize));
		}
		bonus = 0;
	}

	public DiceGroup(long numDice, long dieSize, long bonus) {
		dice = new ArrayList<Die>();
		for (int i = 0; i < numDice; i++) {
			dice.add(new Die(dieSize));
		}
		this.bonus = bonus;
	}

	public void addDie(Die d) {
		dice.add(d);
	}

	public ArrayList<Die> getDice() {
		return dice;
	}

	public void addBonus(long bonus) {
		this.bonus += bonus;
	}

	public long getBonus() {
		return bonus;
	}

	public void roll() {
		for (Die d : dice) {
			d.roll();
		}
	}

	public long getSum() {
		long sum = 0;
		for (Die d : dice) {
			sum += d.getRoll();
		}
		return sum + bonus;
	}

}
