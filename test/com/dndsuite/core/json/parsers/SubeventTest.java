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
import com.dndsuite.core.json.parsers.subevents.AbilityScoreCalculation;
import com.dndsuite.core.json.parsers.subevents.ApplyEffect;
import com.dndsuite.core.json.parsers.subevents.ArmorClassCalculation;
import com.dndsuite.dnd.VirtualBoard;
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
		VirtualBoard.clearGameObjects();
	}

	@Test
	@DisplayName("AbilityScoreCalculation")
	@SuppressWarnings("unchecked")
	void test001() {
		s = new AbilityScoreCalculation();
		JSONObject sJson;
		JSONObject oJson;

		oJson = new JSONObject();
		JSONObject abilityScores = new JSONObject();
		abilityScores.put("str", 10);
		oJson.put("ability_scores", abilityScores);
		oJson.put("effects", new JSONArray());
		source = new GameObject(oJson);

		sJson = new JSONObject();
		sJson.put("subevent", "ability_score_calculation");
		sJson.put("ability", "str");

		try {
			AbilityScoreCalculation asc = (AbilityScoreCalculation) s;
			// normal ability score reading
			asc.parse(sJson, null, source, source);
			assertEquals(10, asc.get());

			// ability score w/ bonus
			asc.parse(sJson, null, source, source);
			asc.addBonus(2);
			asc.addBonus(7);
			assertEquals(19, asc.get());

			// ability score w/ set
			asc.parse(sJson, null, source, source);
			asc.setTo(5);
			assertEquals(5, asc.get());

			// ability score w/ set & bonus
			asc.parse(sJson, null, source, source);
			asc.addBonus(2);
			asc.setTo(20);
			assertEquals(20, asc.get());

			// ability score w/ multiple sets
			asc.parse(sJson, null, source, source);
			asc.setTo(2);
			asc.setTo(20);
			assertEquals(20, asc.get());

		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		}
	}

	@Test
	@DisplayName("ApplyEffect")
	@SuppressWarnings("unchecked")
	void test002() {
		s = new ApplyEffect();
		JSONObject sourceJson;
		JSONObject targetJson;
		JSONObject sJson;

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

		sJson = new JSONObject();
		sJson.put("subevent", "apply_effect");
		sJson.put("effect", "test_effect");
		sJson.put("apply_to", "source");

		try {
			s.parse(sJson, null, source, target);
			JSONArray effects = (JSONArray) source.getJSONData().get("effects");
			assertEquals(1, effects.size());
			assertEquals(3, UUIDTable.size());
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		}

		// reset
		UUIDTable.clear();
		VirtualBoard.clearGameObjects();

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
			s.parse(sJson, null, source, target);
			JSONArray effects = (JSONArray) target.getJSONData().get("effects");
			assertEquals(1, effects.size());
			assertEquals(3, UUIDTable.size());
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		}
	}

	@Test
	@DisplayName("ArmorClassCalculation")
	@SuppressWarnings("unchecked")
	void test003() {
		s = new ArmorClassCalculation();
		JSONObject sJson;
		JSONObject oJson;

		oJson = new JSONObject();
		JSONObject abilityScores = new JSONObject();
		abilityScores.put("dex", 14);
		oJson.put("ability_scores", abilityScores);
		oJson.put("effects", new JSONArray());
		source = new GameObject(oJson);

		sJson = new JSONObject();
		sJson.put("subevent", "armor_class_calculation");

		try {
			ArmorClassCalculation acc = (ArmorClassCalculation) s;
			// Default armor class
			acc.parse(sJson, null, source, source);
			assertEquals(12, acc.get());

			// armor class w/ bonus
			acc.parse(sJson, null, source, source);
			acc.addBonus(2);
			acc.addBonus(5);
			assertEquals(19, acc.get());

			// armor class w/ set
			acc.parse(sJson, null, source, source);
			acc.setTo(13);
			assertEquals(13, acc.get());

			// armor class w/ set & bonus
			acc.parse(sJson, null, source, source);
			acc.setTo(13);
			acc.addBonus(7);
			assertEquals(13, acc.get());

			// armor class w/ multiple sets
			acc.parse(sJson, null, source, source);
			acc.setTo(13);
			acc.setTo(7);
			assertEquals(13, acc.get());

		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		}

	}

}
