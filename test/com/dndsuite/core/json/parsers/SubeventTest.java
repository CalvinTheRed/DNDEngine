package com.dndsuite.core.json.parsers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import com.dndsuite.core.events.Event;
import com.dndsuite.core.gameobjects.GameObject;
import com.dndsuite.core.json.parsers.subevents.AbilityScoreCalculation;
import com.dndsuite.core.json.parsers.subevents.ApplyEffect;
import com.dndsuite.core.json.parsers.subevents.ArmorClassCalculation;
import com.dndsuite.core.json.parsers.subevents.AttackRoll;
import com.dndsuite.dnd.VirtualBoard;
import com.dndsuite.exceptions.SubeventMismatchException;
import com.dndsuite.maths.dice.Die;

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
		abilityScores.put("str", 10L);
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
			assertEquals(10L, asc.get());

			// ability score w/ bonus
			asc.parse(sJson, null, source, source);
			asc.addBonus(2L);
			asc.addBonus(7L);
			assertEquals(19L, asc.get());

			// ability score w/ set
			asc.parse(sJson, null, source, source);
			asc.setTo(5L);
			assertEquals(5L, asc.get());

			// ability score w/ set & bonus
			asc.parse(sJson, null, source, source);
			asc.addBonus(2L);
			asc.setTo(20L);
			assertEquals(20L, asc.get());

			// ability score w/ multiple sets
			asc.parse(sJson, null, source, source);
			asc.setTo(2L);
			asc.setTo(20L);
			assertEquals(20L, asc.get());

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
		abilityScores.put("dex", 14L);
		oJson.put("ability_scores", abilityScores);
		oJson.put("effects", new JSONArray());
		source = new GameObject(oJson);

		sJson = new JSONObject();
		sJson.put("subevent", "armor_class_calculation");

		try {
			ArmorClassCalculation acc = (ArmorClassCalculation) s;
			// Default armor class
			acc.parse(sJson, null, source, source);
			assertEquals(12L, acc.get());

			// armor class w/ bonus
			acc.parse(sJson, null, source, source);
			acc.addBonus(2L);
			acc.addBonus(5L);
			assertEquals(19L, acc.get());

			// armor class w/ set
			acc.parse(sJson, null, source, source);
			acc.setTo(13L);
			assertEquals(13L, acc.get());

			// armor class w/ set & bonus
			acc.parse(sJson, null, source, source);
			acc.setTo(13L);
			acc.addBonus(7L);
			assertEquals(13L, acc.get());

			// armor class w/ multiple sets
			acc.parse(sJson, null, source, source);
			acc.setTo(13L);
			acc.setTo(7L);
			assertEquals(13L, acc.get());

		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		}

	}

	@Test
	@DisplayName("AttackRoll")
	@SuppressWarnings("unchecked")
	void test004() {
		JSONObject sJson;
		JSONObject sourceJson;
		JSONObject targetJson;
		JSONObject eJson;
		AttackRoll ar;
		Event e;

		JSONObject abilityScores = new JSONObject();
		abilityScores.put("str", 10L);
		abilityScores.put("dex", 10L);
		abilityScores.put("con", 10L);
		abilityScores.put("int", 10L);
		abilityScores.put("wis", 10L);
		abilityScores.put("cha", 10L);

		sourceJson = new JSONObject();
		sourceJson.put("ability_scores", abilityScores);
		sourceJson.put("effects", new JSONArray());
		source = new GameObject(sourceJson);

		targetJson = new JSONObject();
		targetJson.put("ability_scores", abilityScores);
		targetJson.put("effects", new JSONArray());
		target = new GameObject(targetJson);

		sJson = new JSONObject();
		sJson.put("subevent", "attack_roll");
		sJson.put("attack_ability", "str");
		JSONArray hit = new JSONArray();
		JSONObject hitSubevent;
		hitSubevent = new JSONObject();
		hitSubevent.put("subevent", "test_subevent");
		hit.add(hitSubevent);
		sJson.put("hit", hit);
		sJson.put("miss", new JSONArray());

		try {
			// Normal attack roll
			Die.enableDiceControl(new long[] { 10L });
			s = new AttackRoll();
			ar = (AttackRoll) s;
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			ar.parse(sJson, e, source, target);
			assertEquals(10L, ar.get());

			// attack roll w/ bonus
			Die.enableDiceControl(new long[] { 10L });
			s = new AttackRoll();
			ar = (AttackRoll) s;
			ar.addBonus(2L);
			assertEquals(2L, ar.get());
			ar.addBonus(7L);
			assertEquals(9L, ar.get());
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			ar.parse(sJson, e, source, target);
			assertEquals(19L, ar.get());

			// attack roll w/ set
			Die.enableDiceControl(new long[] { 10L });
			s = new AttackRoll();
			ar = (AttackRoll) s;
			ar.setTo(15L);
			assertEquals(15L, ar.get());
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			ar.parse(sJson, e, source, target);
			assertEquals(15L, ar.get());

			// attack roll w/ set & bonus
			Die.enableDiceControl(new long[] { 10L });
			s = new AttackRoll();
			ar = (AttackRoll) s;
			ar.setTo(12L);
			assertEquals(12L, ar.get());
			ar.addBonus(7L);
			assertEquals(19L, ar.get());
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			ar.parse(sJson, e, source, target);
			assertEquals(19L, ar.get());

			// attack roll w/ multiple sets
			Die.enableDiceControl(new long[] { 10L });
			s = new AttackRoll();
			ar = (AttackRoll) s;
			ar.setTo(15L);
			assertEquals(15L, ar.get());
			ar.setTo(7L);
			assertEquals(7L, ar.get());
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			ar.parse(sJson, e, source, target);
			assertEquals(7L, ar.get());

			// critical fail attack roll
			Die.enableDiceControl(new long[] { 1L });
			s = new AttackRoll();
			ar = (AttackRoll) s;
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			assertFalse(ar.hasTag("critical_miss"));
			ar.parse(sJson, e, source, target);
			assertEquals(1L, ar.get());
			assertTrue(e.hasTag("critical_miss"));

			// critical hit attack roll via rolling 20
			Die.enableDiceControl(new long[] { 20L });
			s = new AttackRoll();
			ar = (AttackRoll) s;
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			assertFalse(ar.hasTag("critical_hit"));
			ar.parse(sJson, e, source, target);
			assertEquals(20L, ar.get());
			assertTrue(e.hasTag("critical_hit"));

			// critical hit attack roll via decreasing loaded critical_threshold
			Die.enableDiceControl(new long[] { 19L });
			s = new AttackRoll();
			ar = (AttackRoll) s;
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			sJson.put("critical_threshold", 19L);
			assertFalse(ar.hasTag("critical_hit"));
			ar.parse(sJson, e, source, target);
			sJson.remove("critical_threshold");
			assertEquals(19L, ar.getCriticalThreshold());
			assertEquals(19L, ar.get());
			assertTrue(e.hasTag("critical_hit"));

			// rolling with advantage (1 of 2)
			Die.enableDiceControl(new long[] { 5L, 15L });
			s = new AttackRoll();
			ar = (AttackRoll) s;
			ar.addTag("advantage");
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			ar.parse(sJson, e, source, target);
			assertEquals(15L, ar.get());

			// rolling with advantage (2 of 2)
			Die.enableDiceControl(new long[] { 15L, 5L });
			s = new AttackRoll();
			ar = (AttackRoll) s;
			ar.addTag("advantage");
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			ar.parse(sJson, e, source, target);
			assertEquals(15L, ar.get());

			// rolling with disadvantage (1 of 2)
			Die.enableDiceControl(new long[] { 5L, 15L });
			s = new AttackRoll();
			ar = (AttackRoll) s;
			ar.addTag("disadvantage");
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			ar.parse(sJson, e, source, target);
			assertEquals(5L, ar.get());

			// rolling with disadvantage (2 of 2)
			Die.enableDiceControl(new long[] { 15L, 5L });
			s = new AttackRoll();
			ar = (AttackRoll) s;
			ar.addTag("disadvantage");
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			ar.parse(sJson, e, source, target);
			assertEquals(5L, ar.get());

		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		}
	}

}
