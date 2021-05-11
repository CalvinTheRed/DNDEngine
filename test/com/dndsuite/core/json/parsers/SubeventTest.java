package com.dndsuite.core.json.parsers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.dndsuite.core.UUIDTable;
import com.dndsuite.core.gameobjects.GameObject;
import com.dndsuite.core.json.parsers.subevents.ApplyEffect;
import com.dndsuite.exceptions.SubeventMismatchException;

class SubeventTest {
	private static Subevent s;
	private static GameObject source;
	private static GameObject target;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
		s = null;
		source = null;
		target = null;
		UUIDTable.clear();
	}

	@Test
	@DisplayName("ApplyEffect")
	@SuppressWarnings("unchecked")
	void test001() {
		s = new ApplyEffect();
		JSONObject sourceJson;
		JSONObject targetJson;

		/*
		 * { "subevent": "apply_effect", "effect": "test_effect", "apply_to": "source" }
		 */
		sourceJson = new JSONObject();
		sourceJson.put("effects", new JSONArray());
		source = new GameObject(sourceJson);
		UUIDTable.addToTable(source);

		targetJson = new JSONObject();
		targetJson.put("effects", new JSONArray());
		target = new GameObject(targetJson);
		UUIDTable.addToTable(target);

		JSONObject sJson = new JSONObject();
		sJson.put("subevent", "apply_effect");
		sJson.put("effect", "test_effect");
		sJson.put("apply_to", "source");

		try {
			s.parse(sJson, source, target);
			JSONArray effects = (JSONArray) source.getJSONData().get("effects");
			assertEquals(1, effects.size());
			assertEquals(3, UUIDTable.size());
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		}

		// Clear UUIDTable
		UUIDTable.clear();

		/*
		 * { "subevent": "apply_effect", "effect": "test_effect", "apply_to": "target" }
		 */
		sourceJson = new JSONObject();
		sourceJson.put("effects", new JSONArray());
		source = new GameObject(sourceJson);
		UUIDTable.addToTable(source);

		targetJson = new JSONObject();
		targetJson.put("effects", new JSONArray());
		target = new GameObject(targetJson);
		UUIDTable.addToTable(target);

		sJson = new JSONObject();
		sJson.put("subevent", "apply_effect");
		sJson.put("effect", "test_effect");
		sJson.put("apply_to", "target");

		try {
			s.parse(sJson, source, target);
			JSONArray effects = (JSONArray) target.getJSONData().get("effects");
			assertEquals(1, effects.size());
			assertEquals(3, UUIDTable.size());
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		}
	}

}
