package com.dndsuite.exceptions;

public class UUIDDoesNotExistException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6597750286069027407L;

	public UUIDDoesNotExistException(long uuid) {
		super("UUID " + uuid + " is not found in the UUIDTable");
	}
}
