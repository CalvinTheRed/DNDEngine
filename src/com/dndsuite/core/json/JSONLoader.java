package com.dndsuite.core.json;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public abstract class JSONLoader {
	protected JSONObject json;
	protected String file;

	public JSONLoader(JSONObject json) {
		this.json = json;
	}

	public JSONLoader(String file) {
		this.file = file;
		loadFile();
	}

	public JSONObject getJSONData() {
		return json;
	}

	private void loadFile() {
		JSONParser jsonParser = new JSONParser();
		try (FileReader reader = new FileReader(file)) {
			json = (JSONObject) jsonParser.parse(reader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
