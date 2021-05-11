package com.dndsuite.exceptions;

public class ConditionMismatchException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5137609855464183958L;

	public ConditionMismatchException(String expected, String found) {
		super("ConditionMismatchException: expected " + expected + " but found " + found);
	}

}
