package com.dndsuite.exceptions;

public class JSONValueException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9003356521264006370L;

	public JSONValueException(String value) {
		super("JSONValueException: " + value + " is not a valid value");
	}

}
