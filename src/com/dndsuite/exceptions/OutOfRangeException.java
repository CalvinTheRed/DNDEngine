package com.dndsuite.exceptions;

public class OutOfRangeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9174269574693050914L;

	public OutOfRangeException() {
		super("An Event attempted to target a location beyond its allowed range");
	}
}
