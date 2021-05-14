package com.dndsuite.core.json;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.dndsuite.core.Taggable;
import com.dndsuite.core.UUIDTableElement;
import com.dndsuite.exceptions.UUIDNotAssignedException;

public abstract class JSONLoader implements Taggable {
	protected JSONObject json;

	/**
	 * This constructor to be used in tandem with JSONLoader.loadFromFile
	 * 
	 * @param json
	 */
	public JSONLoader(JSONObject json) {
		this.json = json;
	}

	/**
	 * This constructor to be used for loading from resource files but not from save
	 * files
	 * 
	 * @param file
	 */
	public JSONLoader(String file) {
		JSONParser jsonParser = new JSONParser();
		try (FileReader reader = new FileReader("resources/json/" + file + ".json")) {
			json = (JSONObject) jsonParser.parse(reader);
			parseResourceData();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This function to be used for loading from save files
	 * 
	 * @param file
	 * @return
	 */
	public static JSONObject loadFromFile(String file) {
		JSONParser jsonParser = new JSONParser();
		try (FileReader reader = new FileReader(file + ".json")) {
			return (JSONObject) jsonParser.parse(reader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected abstract void parseResourceData();

	public JSONObject getJSONData() {
		return json;
	}

	@Override
	@SuppressWarnings("unchecked")
	public String toString() {
		String name = (String) json.getOrDefault("name", "<?>");
		try {
			if (this instanceof UUIDTableElement) {
				UUIDTableElement element = (UUIDTableElement) this;
				name += " (" + element.getUUID() + ")";
			}
		} catch (UUIDNotAssignedException ex) {
			ex.printStackTrace();
		}

		return name;
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
