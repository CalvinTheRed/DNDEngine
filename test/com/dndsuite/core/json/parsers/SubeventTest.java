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
		abilityScores.put("str", 10);
		abilityScores.put("dex", 10);
		abilityScores.put("con", 10);
		abilityScores.put("int", 10);
		abilityScores.put("wis", 10);
		abilityScores.put("cha", 10);

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
			Die.enableDiceControl(new int[] { 10 });
			s = new AttackRoll();
			ar = (AttackRoll) s;
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			ar.parse(sJson, e, source, target);
			assertEquals(10, ar.get());

			// attack roll w/ bonus
			Die.enableDiceControl(new int[] { 10 });
			s = new AttackRoll();
			ar = (AttackRoll) s;
			ar.addBonus(2);
			assertEquals(2, ar.get());
			ar.addBonus(7);
			assertEquals(9, ar.get());
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			ar.parse(sJson, e, source, target);
			assertEquals(19, ar.get());

			// attack roll w/ set
			Die.enableDiceControl(new int[] { 10 });
			s = new AttackRoll();
			ar = (AttackRoll) s;
			ar.setTo(15);
			assertEquals(15, ar.get());
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			ar.parse(sJson, e, source, target);
			assertEquals(15, ar.get());

			// attack roll w/ set & bonus
			Die.enableDiceControl(new int[] { 10 });
			s = new AttackRoll();
			ar = (AttackRoll) s;
			ar.setTo(12);
			assertEquals(12, ar.get());
			ar.addBonus(7);
			assertEquals(19, ar.get());
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			ar.parse(sJson, e, source, target);
			assertEquals(19, ar.get());

			// attack roll w/ multiple sets
			Die.enableDiceControl(new int[] { 10 });
			s = new AttackRoll();
			ar = (AttackRoll) s;
			ar.setTo(15);
			assertEquals(15, ar.get());
			ar.setTo(7);
			assertEquals(7, ar.get());
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			ar.parse(sJson, e, source, target);
			assertEquals(7, ar.get());

			// critical fail attack roll
			Die.enableDiceControl(new int[] { 1 });
			s = new AttackRoll();
			ar = (AttackRoll) s;
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			assertFalse(ar.hasTag("critical_miss"));
			ar.parse(sJson, e, source, target);
			assertEquals(1, ar.get());
			assertTrue(e.hasTag("critical_miss"));

			// critical hit attack roll via rolling 20
			Die.enableDiceControl(new int[] { 20 });
			s = new AttackRoll();
			ar = (AttackRoll) s;
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			assertFalse(ar.hasTag("critical_hit"));
			ar.parse(sJson, e, source, target);
			assertEquals(20, ar.get());
			assertTrue(e.hasTag("critical_hit"));

			// critical hit attack roll via decreasing loaded critical_threshold
			Die.enableDiceControl(new int[] { 19 });
			s = new AttackRoll();
			ar = (AttackRoll) s;
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			sJson.put("critical_threshold", 19);
			assertFalse(ar.hasTag("critical_hit"));
			ar.parse(sJson, e, source, target);
			sJson.remove("critical_threshold");
			assertEquals(19, ar.getCriticalThreshold());
			assertEquals(19, ar.get());
			assertTrue(e.hasTag("critical_hit"));

			// rolling with advantage (1 of 2)
			Die.enableDiceControl(new int[] { 5, 15 });
			s = new AttackRoll();
			ar = (AttackRoll) s;
			ar.addTag("advantage");
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			ar.parse(sJson, e, source, target);
			assertEquals(15, ar.get());

			// rolling with advantage (2 of 2)
			Die.enableDiceControl(new int[] { 15, 5 });
			s = new AttackRoll();
			ar = (AttackRoll) s;
			ar.addTag("advantage");
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			ar.parse(sJson, e, source, target);
			assertEquals(15, ar.get());

			// rolling with disadvantage (1 of 2)
			Die.enableDiceControl(new int[] { 5, 15 });
			s = new AttackRoll();
			ar = (AttackRoll) s;
			ar.addTag("disadvantage");
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			ar.parse(sJson, e, source, target);
			assertEquals(5, ar.get());

			// rolling with disadvantage (1 of 2)
			Die.enableDiceControl(new int[] { 15, 5 });
			s = new AttackRoll();
			ar = (AttackRoll) s;
			ar.addTag("disadvantage");
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			ar.parse(sJson, e, source, target);
			assertEquals(5, ar.get());

		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		}
	}

}
