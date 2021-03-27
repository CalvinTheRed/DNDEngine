package com.dndsuite.maths.dice;

import java.util.Random;

/**
 * This class represents a fair die of N many sides
 * 
 * @author calvi
 *
 */
public class Die {
	public static final int CRITICAL_FAIL = 1;
	public static final int CRITICAL_HIT = 20;

	protected int size;
	protected int roll;

	private static final Random r = new Random();

	/**
	 * Constructor for class Die
	 * 
	 * @param size ({@code int}) the number of faces on the die
	 */
	public Die(int size) {
		this.size = size;
		roll = 0;
	}

	/**
	 * This function rolls the Die
	 */
	public void roll() {
//		if (size == 20) {
//			roll = CRITICAL_FAIL;
//			return;
//		}
		roll = Die.r.nextInt(size) + 1;
	}

	/**
	 * This function returns the number of faces the Die has
	 * 
	 * @return {@code int} size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * This function returns the last rolled value of the Die. If the Die has never
	 * been rolled, this function returns 0
	 * 
	 * @return {@code int} last roll
	 */
	public int getRoll() {
		return roll;
	}

	/**
	 * This function returns a deep clone of the original Die object. The cloneis of
	 * the same size and has the same value for the most recent roll
	 * 
	 * @return {@code Die} clone
	 * 
	 * @Override
	 */
	public Die clone() {
		Die clone = new Die(size);
		clone.roll = roll;
		return clone;
	}

	public void upsize() {
		size += 2;
	}
}
