package com.dndsuite.exceptions;

import com.dndsuite.core.Item;

public class CannotUnequipItemException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2912368651649173029L;

	public CannotUnequipItemException(Item item) {
		super("Cannot unequip " + item);
	}
}
