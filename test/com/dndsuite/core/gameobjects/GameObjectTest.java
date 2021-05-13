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
import com.dndsuite.dnd.VirtualBoard;
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
		VirtualBoard.clearGameObjects();
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
				long uuid = (long) effectUUIDs.get(i);
				assertTrue(UUIDTable.containsKey(uuid));
			}

			// UUIDTable contains UUID of o's tasks
			JSONArray taskUUIDs = (JSONArray) o.getJSONData().get("tasks");
			for (int i = 0; i < taskUUIDs.size(); i++) {
				long uuid = (long) taskUUIDs.get(i);
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
		abilityScores.put("str", 7L);
		abilityScores.put("dex", 8L);
		abilityScores.put("con", 9L);
		abilityScores.put("int", 10L);
		abilityScores.put("wis", 11L);
		abilityScores.put("cha", 12L);
		json.put("ability_scores", abilityScores);
		json.put("effects", new JSONArray());

		o = new GameObject(json);

		assertEquals(-2L, o.getAbilityModifier("str"));
		assertEquals(-1L, o.getAbilityModifier("dex"));
		assertEquals(-1L, o.getAbilityModifier("con"));
		assertEquals(0L, o.getAbilityModifier("int"));
		assertEquals(0L, o.getAbilityModifier("wis"));
		assertEquals(1L, o.getAbilityModifier("cha"));
	}

	@Test
	@DisplayName("Taking Damage")
	@SuppressWarnings("unchecked")
	void test003() {

	}

}
