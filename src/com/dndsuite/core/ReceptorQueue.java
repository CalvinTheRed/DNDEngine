package com.dndsuite.core;

import java.util.ArrayList;

public final class ReceptorQueue {
	public static final boolean FREE = true;
	public static final boolean WAITING = false;

	private static ArrayList<Receptor> inputQueue = new ArrayList<Receptor>();
	private static boolean status = FREE;

	public static void enqueue(Receptor r) {
		inputQueue.add(r);
		status = WAITING;
	}

	public static Receptor dequeue() {
		Receptor r = inputQueue.remove(0);
		if (inputQueue.size() == 0) {
			status = FREE;
		}
		return r;
	}

	public boolean getStatus() {
		return status;
	}

}
