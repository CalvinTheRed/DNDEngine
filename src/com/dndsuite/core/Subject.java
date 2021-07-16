package com.dndsuite.core;

/**
 * Subject is an interface which represents an object's ability to be observed
 * by Observers, and to notify them whenever a relevant change takes place.
 * 
 * @author Calvin Withun
 *
 */
public interface Subject {

	public abstract void addObserver(Observer o);

	public abstract void removeObserver(Observer o);

	/**
	 * This function allows the Subject to notify its Obervers that it has undergone
	 * a relevant change, in order for the Observers to respond to that change.
	 */
	public abstract void updateObservers();

}
