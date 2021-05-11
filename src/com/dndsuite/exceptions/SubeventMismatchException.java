package com.dndsuite.exceptions;

public class SubeventMismatchException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3634187262407361480L;

	public SubeventMismatchException(String expected, String found) {
		super("SubeventMismatchException: expected " + expected + " but found " + found);
	}

}
