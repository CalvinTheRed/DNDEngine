package com.dndsuite.exceptions;

public class FunctionMismatchException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4133193985514617713L;

	public FunctionMismatchException(String expected, String found) {
		super("FunctionMismatchException: expected " + expected + " but found " + found);
	}

}
