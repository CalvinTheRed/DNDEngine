package com.dndsuite.maths.dice;

import java.util.Random;

public class Die {
	private static boolean controlEnabled = false;
	private static int queuePos;
	private static long[] valueQueue;

	protected long size;
	protected long roll;

	private static final Random r = new Random();

	public Die(long size) {
		this.size = size;
		roll = 0;
	}

	public void roll() {
		if (controlEnabled && queuePos == valueQueue.length) {
			disableDiceControl();
		}
		if (controlEnabled) {
			roll = valueQueue[queuePos];
			queuePos++;
		} else {
			roll = Die.r.nextInt((int) size) + 1;
		}
	}

	public long getSize() {
		return size;
	}

	public long getRoll() {
		return roll;
	}

	public Die clone() {
		Die clone = new Die(size);
		clone.roll = roll;
		return clone;
	}

	public void upsize() {
		size += 2;
	}

	public static void enableDiceControl(long[] queue) {
		controlEnabled = true;
		queuePos = 0;
		valueQueue = queue;
	}

	private static void disableDiceControl() {
		controlEnabled = false;
	}

}
