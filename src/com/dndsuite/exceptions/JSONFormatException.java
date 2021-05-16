package com.dndsuite.exceptions;

public class JSONFormatException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1654830330198979738L;

	public JSONFormatException() {
		super("There was a JSON format error");
	}
}
