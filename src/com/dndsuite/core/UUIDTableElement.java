package com.dndsuite.core;

import com.dndsuite.exceptions.UUIDNotAssignedException;

public interface UUIDTableElement {

	public abstract long getUUID() throws UUIDNotAssignedException;

	public abstract void assignUUID(long uuid);

}
