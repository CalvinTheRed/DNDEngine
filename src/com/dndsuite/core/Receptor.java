package com.dndsuite.core;

import org.json.simple.JSONObject;

import com.dndsuite.exceptions.JSONFormatException;

public interface Receptor {

	public abstract void pause(JSONObject pauseNotes);

	public abstract void resume(JSONObject json) throws JSONFormatException;

	public abstract JSONObject getPauseNotes();

}
