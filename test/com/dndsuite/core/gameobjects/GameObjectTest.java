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

class GameObjectTest {

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
		UUIDTable.clear();
		VirtualBoard.clearGameObjects();
	}

	@Test
	@DisplayName("UUIDTable tests")
	void test001() {
		GameObject o = new GameObject(new JSONObject());
		UUIDTable.addToTable(o);

		try {
			assertTrue(UUIDTable.containsKey(o.getUUID()));
		} catch (UUIDKeyMissingException ex) {
			ex.printStackTrace();
			fail("UUID key(s) missing");
		}
	}

	@Test
	@DisplayName("Ability Score Modifiers")
	@SuppressWarnings("unchecked")
	void test002() {
		GameObject o;
		JSONObject oJson;

		oJson = new JSONObject();
		JSONArray pos = new JSONArray();
		pos.add(0L);
		pos.add(0L);
		pos.add(0L);
		oJson.put("pos", pos);
		oJson.put("tags", new JSONArray());
		JSONObject abilityScores = new JSONObject();
		abilityScores.put("str", 7L);
		abilityScores.put("dex", 8L);
		abilityScores.put("con", 9L);
		abilityScores.put("int", 10L);
		abilityScores.put("wis", 11L);
		abilityScores.put("cha", 12L);
		oJson.put("ability_scores", abilityScores);
		oJson.put("effects", new JSONArray());

		o = new GameObject(oJson);

		assertEquals(-2L, o.getAbilityModifier("str"));
		assertEquals(-1L, o.getAbilityModifier("dex"));
		assertEquals(-1L, o.getAbilityModifier("con"));
		assertEquals(0L, o.getAbilityModifier("int"));
		assertEquals(0L, o.getAbilityModifier("wis"));
		assertEquals(1L, o.getAbilityModifier("cha"));
	}

}
