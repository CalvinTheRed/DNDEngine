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
import com.dndsuite.core.json.parsers.subevents.ArmorClassCalculation;
import com.dndsuite.core.json.parsers.subevents.AttackRoll;
import com.dndsuite.dnd.VirtualBoard;
import com.dndsuite.exceptions.SubeventMismatchException;
import com.dndsuite.maths.dice.Die;

class SubeventTest {

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
	@DisplayName("AbilityScoreCalculation")
	@SuppressWarnings("unchecked")
	void test001() {
		Subevent s = new AbilityScoreCalculation();
		JSONObject sJson;
		JSONObject oJson;
		GameObject o;

		oJson = new JSONObject();
		JSONObject abilityScores = new JSONObject();
		abilityScores.put("str", 10L);
		oJson.put("ability_scores", abilityScores);
		oJson.put("effects", new JSONArray());
		o = new GameObject(oJson);

		sJson = new JSONObject();
		sJson.put("subevent", "ability_score_calculation");
		sJson.put("ability", "str");

		try {
			AbilityScoreCalculation asc = (AbilityScoreCalculation) s;
			// normal ability score reading
			asc.parse(sJson, null, o, o);
			assertEquals(10L, asc.get());

			// ability score w/ bonus
			asc.parse(sJson, null, o, o);
			asc.addBonus(2L);
			asc.addBonus(7L);
			assertEquals(19L, asc.get());

			// ability score w/ set
			asc.parse(sJson, null, o, o);
			asc.setTo(5L);
			assertEquals(5L, asc.get());

			// ability score w/ set & bonus
			asc.parse(sJson, null, o, o);
			asc.addBonus(2L);
			asc.setTo(20L);
			assertEquals(20L, asc.get());

			// ability score w/ multiple sets
			asc.parse(sJson, null, o, o);
			asc.setTo(2L);
			asc.setTo(20L);
			assertEquals(20L, asc.get());

		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		}
	}

	@Test
	@DisplayName("ArmorClassCalculation")
	@SuppressWarnings("unchecked")
	void test003() {
		Subevent s = new ArmorClassCalculation();
		JSONObject sJson;
		JSONObject oJson;
		GameObject o;

		oJson = new JSONObject();
		JSONObject abilityScores = new JSONObject();
		abilityScores.put("dex", 14L);
		oJson.put("ability_scores", abilityScores);
		oJson.put("effects", new JSONArray());
		o = new GameObject(oJson);

		sJson = new JSONObject();
		sJson.put("subevent", "armor_class_calculation");

		try {
			ArmorClassCalculation acc = (ArmorClassCalculation) s;
			// Default armor class
			acc.parse(sJson, null, o, o);
			assertEquals(12L, acc.get());

			// armor class w/ bonus
			acc.parse(sJson, null, o, o);
			acc.addBonus(2L);
			acc.addBonus(5L);
			assertEquals(19L, acc.get());

			// armor class w/ set
			acc.parse(sJson, null, o, o);
			acc.setTo(13L);
			assertEquals(13L, acc.get());

			// armor class w/ set & bonus
			acc.parse(sJson, null, o, o);
			acc.setTo(13L);
			acc.addBonus(7L);
			assertEquals(13L, acc.get());

			// armor class w/ multiple sets
			acc.parse(sJson, null, o, o);
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
		AttackRoll s;
		JSONObject sJson;
		JSONObject oJson;
		JSONObject eJson;
		GameObject o;
		Event e;

		JSONObject abilityScores = new JSONObject();
		abilityScores.put("str", 10L);
		abilityScores.put("dex", 10L);
		abilityScores.put("con", 10L);
		abilityScores.put("int", 10L);
		abilityScores.put("wis", 10L);
		abilityScores.put("cha", 10L);

		oJson = new JSONObject();
		oJson.put("ability_scores", abilityScores);
		oJson.put("effects", new JSONArray());
		o = new GameObject(oJson);

		sJson = new JSONObject();
		sJson.put("subevent", "attack_roll");
		sJson.put("attack_ability", "str");
		sJson.put("hit", new JSONArray());
		sJson.put("miss", new JSONArray());

		try {
			// Normal attack roll
			Die.enableDiceControl(new long[] { 10L });
			s = new AttackRoll();
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			s.parse(sJson, e, o, o);
			assertEquals(10L, s.get());

			// attack roll w/ bonus
			Die.enableDiceControl(new long[] { 10L });
			s = new AttackRoll();
			s.addBonus(2L);
			assertEquals(2L, s.get());
			s.addBonus(7L);
			assertEquals(9L, s.get());
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			s.parse(sJson, e, o, o);
			assertEquals(19L, s.get());

			// attack roll w/ set
			Die.enableDiceControl(new long[] { 10L });
			s = new AttackRoll();
			s.setTo(15L);
			assertEquals(15L, s.get());
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			s.parse(sJson, e, o, o);
			assertEquals(15L, s.get());

			// attack roll w/ set & bonus
			Die.enableDiceControl(new long[] { 10L });
			s = new AttackRoll();
			s.setTo(12L);
			assertEquals(12L, s.get());
			s.addBonus(7L);
			assertEquals(19L, s.get());
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			s.parse(sJson, e, o, o);
			assertEquals(19L, s.get());

			// attack roll w/ multiple sets
			Die.enableDiceControl(new long[] { 10L });
			s = new AttackRoll();
			s.setTo(15L);
			assertEquals(15L, s.get());
			s.setTo(7L);
			assertEquals(7L, s.get());
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			s.parse(sJson, e, o, o);
			assertEquals(7L, s.get());

			// critical fail attack roll
			Die.enableDiceControl(new long[] { 1L });
			s = new AttackRoll();
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			assertFalse(s.hasTag("critical_miss"));
			s.parse(sJson, e, o, o);
			assertEquals(1L, s.get());
			assertTrue(e.hasTag("critical_miss"));

			// critical hit attack roll via rolling 20
			Die.enableDiceControl(new long[] { 20L });
			s = new AttackRoll();
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			assertFalse(s.hasTag("critical_hit"));
			s.parse(sJson, e, o, o);
			assertEquals(20L, s.get());
			assertTrue(e.hasTag("critical_hit"));

			// critical hit attack roll via decreasing loaded critical_threshold
			Die.enableDiceControl(new long[] { 19L });
			s = new AttackRoll();
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			sJson.put("critical_threshold", 19L);
			assertFalse(s.hasTag("critical_hit"));
			s.parse(sJson, e, o, o);
			sJson.remove("critical_threshold");
			assertEquals(19L, s.getCriticalThreshold());
			assertEquals(19L, s.get());
			assertTrue(e.hasTag("critical_hit"));

			// rolling with advantage (1 of 2)
			Die.enableDiceControl(new long[] { 5L, 15L });
			s = new AttackRoll();
			s.addTag("advantage");
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			s.parse(sJson, e, o, o);
			assertEquals(15L, s.get());

			// rolling with advantage (2 of 2)
			Die.enableDiceControl(new long[] { 15L, 5L });
			s = new AttackRoll();
			s.addTag("advantage");
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			s.parse(sJson, e, o, o);
			assertEquals(15L, s.get());

			// rolling with disadvantage (1 of 2)
			Die.enableDiceControl(new long[] { 5L, 15L });
			s = new AttackRoll();
			s.addTag("disadvantage");
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			s.parse(sJson, e, o, o);
			assertEquals(5L, s.get());

			// rolling with disadvantage (2 of 2)
			Die.enableDiceControl(new long[] { 15L, 5L });
			s = new AttackRoll();
			s.addTag("disadvantage");
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			s.parse(sJson, e, o, o);
			assertEquals(5L, s.get());

		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		}
	}

}
