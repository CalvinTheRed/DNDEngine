package com.dndsuite.exceptions;

import com.dndsuite.core.UUIDTableElement;

public class UUIDNotAssignedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8811104818832423444L;

	public UUIDNotAssignedException(UUIDTableElement e) {
		super("UUID not assigned to " + e);
	}

}
