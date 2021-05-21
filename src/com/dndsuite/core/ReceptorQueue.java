package com.dndsuite.core;

import java.util.ArrayList;

/**
 * ReceptorQueue is a class which contains all Receptor objects which have yet
 * to be given a response from the user/client. This class serves as a FIFO
 * queue, such that the Receptors are retrieved in the same order as they were
 * queued.
 * 
 * @author Calvin Withun
 *
 */
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
		status = inputQueue.size() == 0;
		return r;
	}

	/**
	 * This function indicates whether there are any Receptor objects which require
	 * user input.
	 * 
	 * @return true if there are no unprocessed Receptors, false if there are
	 *         unprocessed Receptors
	 */
	public boolean checkQueue() {
		return status;
	}

}
