package com.dndsuite.core.json.parsers.subevents;

/**
 * Calculation is an interface which represents the ability to add bonuses to
 * some value or set that value to a particular value.
 * 
 * @author Calvin Withun
 *
 */
public interface Calculation {

	public abstract void addBonus(long bonus);

	public abstract void setTo(long set);

	public abstract long get();

	public abstract long getRaw();

}
