package com.dndsuite.exceptions;

public class UUIDKeyMissingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4383460791310555341L;

	public UUIDKeyMissingException() {
		super("JSON data does not contain key 'uuid'");
	}

}
