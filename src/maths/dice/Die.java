package maths.dice;

import java.util.Random;

public class Die {
	protected int size;
	protected int roll;
	
	private static final Random r = new Random();
	
	public Die(int size) {
		this.size = size;
	}
	
	public void roll() {
		roll = Die.r.nextInt(size) + 1; 
	}
	
	public int getRoll() {
		return roll;
	}
	
	@Override
	public Die clone() {
		Die d = new Die(size);
		d.roll = roll;
		return d;
	}
}
