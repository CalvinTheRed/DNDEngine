package com.dndsuite.exceptions;

public class InvalidAreaOfEffectException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3721600799292188906L;

	public InvalidAreaOfEffectException() {
		super("Area of Effect parameters were not provided");
	}
}
