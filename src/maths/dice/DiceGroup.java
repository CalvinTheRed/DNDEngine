package maths.dice;

import java.util.LinkedList;

public class DiceGroup {
	protected LinkedList<Die> dice;
	protected int bonus;

	public DiceGroup(int numDice, int dieSize) {
		dice = new LinkedList<Die>();
		for (int i = 0; i < numDice; i++) {
			dice.add(new Die(dieSize));
		}
		bonus = 0;
	}

	public void addDie(Die d) {
		dice.add(d);
	}

	public LinkedList<Die> getDice() {
		return dice;
	}

	public void addBonus(int bonus) {
		this.bonus += bonus;
	}

	public int getBonus() {
		return bonus;
	}

	public void roll() {
		for (Die d : dice) {
			d.roll();
		}
	}

	public int getSum() {
		int sum = 0;
		for (Die d : dice) {
			sum += d.getRoll();
		}
		return sum + bonus;
	}

}
