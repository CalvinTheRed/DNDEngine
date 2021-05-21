package com.dndsuite.core.json;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.dndsuite.core.Taggable;

/**
 * JSONLoader is an abstract class which represents any data type which can load
 * JSON data from external files or save that data to external files.
 * 
 * @author Calvin Withun
 *
 */
public abstract class JSONLoader implements Taggable {
	protected JSONObject json;

	/**
	 * JSONLoader constructor to be used in tandem with JSONLoader.loadFromFile() to
	 * load save JSON files into the library. This is not to be used for loading
	 * template JSON files.
	 * 
	 * @param json - the JSON data stored in a save file
	 */
	public JSONLoader(JSONObject json) {
		this.json = json;
	}

	/**
	 * JSONLoader constructor to be used to load template JSON files into the
	 * library and parse them accordingly. This is not to be used for loading save
	 * JSON files.
	 * 
	 * @param file - the path to a file, as a continuation of the file path
	 *             "resources/json/..."
	 */
	public JSONLoader(String file) {
		JSONParser jsonParser = new JSONParser();
		try (FileReader reader = new FileReader("resources/json/" + file + ".json")) {
			json = (JSONObject) jsonParser.parse(reader);
			parseTemplate();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This function is designed to load saved JSON data from an arbitrary file.
	 * This function should not be used to load template JSON data.
	 * 
	 * @param file - full file path to the desired save file (do not include file
	 *             extension)
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

	/**
	 * A function which modifies loaded template data, if necessary.
	 */
	protected abstract void parseTemplate();

	public JSONObject getJSONData() {
		return json;
	}

	@Override
	@SuppressWarnings("unchecked")
	public String toString() {
		return (String) json.getOrDefault("name", "<?>");
	}

	@SuppressWarnings("unchecked")
	public void addTag(String tag) {
		JSONArray tags = (JSONArray) json.get("tags");
		tags.add(tag);
	}

	@Override
	public void removeTag(String tag) {
		JSONArray tags = (JSONArray) json.get("tags");
		tags.remove(tag);
	}

	@Override
	public boolean hasTag(String tag) {
		JSONArray tags = (JSONArray) json.get("tags");
		return tags.contains(tag);
	}

}
