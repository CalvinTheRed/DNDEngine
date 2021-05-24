package com.dndsuite.core;

/**
 * UUIDTableElement is an interface which represents any object which can be
 * saved to a save file. All such objects must be stored by the UUIDTable class.
 * 
 * @author Calvin Withun
 *
 */
public interface UUIDTableElement {

	public abstract long getUUID();

	public abstract void assignUUID(long uuid);

}
