package com.dndsuite.core;

/**
 * Observer is an interface which represents the ability of an object to watch a
 * Subject. An Observer has the ability to change itself in response to the
 * actions of a Subject in real-time.
 * 
 * @author Calvin Withun
 *
 */
public interface Observer {

	/**
	 * This function allows the Observer to change itself in response to a change in
	 * its Subject.
	 * 
	 * @param s - the Subject of observation
	 */
	public abstract void update(Subject s);

}
