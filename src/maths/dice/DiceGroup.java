package maths.dice;

import java.util.LinkedList;

public class DiceGroup {
	protected LinkedList<Die> dice;
	
	public DiceGroup(int numDice, int dieSize) {
		dice = new LinkedList<Die>();
		for (int i = 0; i < numDice; i++) {
			dice.add(new Die(dieSize));
		}
	}
	
	public void roll() {
		for (Die d : dice) {
			d.roll();
		}
	}
	
	public LinkedList<Die> getDice(){
		return dice;
	}
	
	public int getSum() {
		int sum = 0;
		for (Die d : dice) {
			sum += d.getRoll();
		}
		return sum;
	}
	
}
