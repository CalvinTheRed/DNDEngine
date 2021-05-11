package com.dndsuite.core.gameobjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import com.dndsuite.exceptions.UUIDKeyMissingException;
import com.dndsuite.maths.Vector;

class GameObjectTest {
	static GameObject o;

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
		o = null;
		UUIDTable.clear();
	}

	@Test
	@DisplayName("UUIDTable tests")
	void test001() {
		o = new GameObject("test_gameobject_source", new Vector(), new Vector());

		try {
			// UUIDTable contains UUID of o
			assertTrue(UUIDTable.containsKey(o.getUUID()));

			// UUIDTable contains UUID of o's effects
			JSONArray effectUUIDs = (JSONArray) o.getJSONData().get("effects");
			for (int i = 0; i < effectUUIDs.size(); i++) {
				int uuid = (int) effectUUIDs.get(i);
				assertTrue(UUIDTable.containsKey(uuid));
			}

			// UUIDTable contains UUID of o's tasks
			JSONArray taskUUIDs = (JSONArray) o.getJSONData().get("tasks");
			for (int i = 0; i < taskUUIDs.size(); i++) {
				int uuid = (int) taskUUIDs.get(i);
				assertTrue(UUIDTable.containsKey(uuid));
			}
		} catch (UUIDKeyMissingException ex) {
			ex.printStackTrace();
			fail("UUID key(s) missing");
		}
	}

	@Test
	@DisplayName("Ability Score Modifiers")
	@SuppressWarnings("unchecked")
	void test002() {
		JSONObject json = new JSONObject();
		JSONObject abilityScores = new JSONObject();
		abilityScores.put("str", 7);
		abilityScores.put("dex", 8);
		abilityScores.put("con", 9);
		abilityScores.put("int", 10);
		abilityScores.put("wis", 11);
		abilityScores.put("cha", 12);
		json.put("ability_scores", abilityScores);
		json.put("effects", new JSONArray());

		o = new GameObject(json);

		assertEquals(-2, o.getAbilityModifier("str"));
		assertEquals(-1, o.getAbilityModifier("dex"));
		assertEquals(-1, o.getAbilityModifier("con"));
		assertEquals(0, o.getAbilityModifier("int"));
		assertEquals(0, o.getAbilityModifier("wis"));
		assertEquals(1, o.getAbilityModifier("cha"));
	}

}
