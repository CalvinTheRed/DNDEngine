package com.dndsuite.core;

import org.json.simple.JSONObject;

import com.dndsuite.exceptions.JSONFormatException;

/**
 * Receptor is an interface which represents an Object which can be initiated
 * automatically, but which requires additional user input before it can
 * precipitate further.
 * 
 * @author Calvin Withun
 *
 */
public interface Receptor {

	public abstract void pause();

	/**
	 * This function allows the Receptor to precipitate its fallout in response to
	 * the provided JSON input.
	 * 
	 * @param json - input data provided by the user/client in the form of
	 *             {"responses":[{response1},{response2},...]}
	 * @throws JSONFormatException if the JSON data provided is formatted in a way
	 *                             which the Receptor cannot make use of, this
	 *                             exception is thrown.
	 */
	public abstract void resume(JSONObject json) throws JSONFormatException;

	/**
	 * This function provides notes generated by the Receptor which indicate what
	 * data is desired by the resume() function.
	 * 
	 * @return a GameObject containing request data in the form of
	 *         {"requests":["request1","request2",...]}
	 */
	public abstract JSONObject getPauseNotes();

}
