package com.dndsuite.core.json;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.dndsuite.core.Taggable;
import com.dndsuite.core.UUIDTable;
import com.dndsuite.exceptions.UUIDKeyMissingException;

public abstract class JSONLoader implements Taggable {
	protected JSONObject json;

	public JSONLoader(JSONObject json) {
		this.json = json;
	}

	public JSONLoader(String file) {
		JSONParser jsonParser = new JSONParser();
		try (FileReader reader = new FileReader("resources/json/" + file + ".json")) {
			json = (JSONObject) jsonParser.parse(reader);
			UUIDTable.addToTable(this);
			parseBasePattern();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	protected abstract void parseBasePattern();

	public JSONObject getJSONData() {
		return json;
	}

	public long getUUID() throws UUIDKeyMissingException {
		if (json.containsKey("uuid")) {
			return (long) json.get("uuid");
		}
		throw new UUIDKeyMissingException();
	}

	@Override
	@SuppressWarnings("unchecked")
	public String toString() {
		return (String) json.getOrDefault("name", "?") + " (" + (long) json.getOrDefault("uuid", -1) + ")";
	}

	@SuppressWarnings("unchecked")
	public void addTag(String tag) {
		JSONArray tags = (JSONArray) json.remove("tags");
		tags.add(tag);
		json.put("tags", tags);
	}

	@SuppressWarnings("unchecked")
	public void removeTag(String tag) {
		JSONArray tags = (JSONArray) json.remove("tags");
		tags.remove(tag);
		json.put("tags", tags);
	}

	public boolean hasTag(String tag) {
		JSONArray tags = (JSONArray) json.get("tags");
		return tags.contains(tag);
	}

}
