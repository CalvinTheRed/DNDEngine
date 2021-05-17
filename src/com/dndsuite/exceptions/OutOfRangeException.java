package com.dndsuite.exceptions;

public class OutOfRangeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9174269574693050914L;

	public OutOfRangeException(double range, double distance) {
		super("OutOfRangeException: target too far away (" + distance + " / " + range + ")");
	}
}
