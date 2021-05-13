package com.dndsuite.core.json.parsers;

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
import com.dndsuite.core.json.parsers.conditions.DealsDamageType;
import com.dndsuite.core.json.parsers.conditions.HasTag;
import com.dndsuite.core.json.parsers.conditions.IsCriticalHit;
import com.dndsuite.core.json.parsers.conditions.IsCriticalMiss;
import com.dndsuite.core.json.parsers.conditions.IsRollAbove;
import com.dndsuite.core.json.parsers.conditions.IsRollBelow;
import com.dndsuite.core.json.parsers.subevents.ArmorClassCalculation;
import com.dndsuite.core.json.parsers.subevents.AttackRoll;
import com.dndsuite.core.json.parsers.subevents.DamageCalculation;
import com.dndsuite.dnd.VirtualBoard;
import com.dndsuite.exceptions.ConditionMismatchException;
import com.dndsuite.exceptions.SubeventMismatchException;
import com.dndsuite.maths.Vector;
import com.dndsuite.maths.dice.Die;

class ConditionTest {

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
	@DisplayName("HasTag")
	@SuppressWarnings("unchecked")
	void test001() {
		Condition c = new HasTag();
		Subevent s = new ArmorClassCalculation();

		JSONObject cJson = new JSONObject();
		cJson.put("condition", "has_tag");
		cJson.put("tag", "test");

		try {
			assertFalse(c.parse(cJson, null, s));
			s.addTag("test");
			assertTrue(c.parse(cJson, null, s));
		} catch (ConditionMismatchException ex) {
			ex.printStackTrace();
			fail("Condition mismatch");
		}
	}

	@Test
	@DisplayName("IsCriticalHit")
	@SuppressWarnings("unchecked")
	void test002() {
		Condition c;
		AttackRoll s;
		GameObject o;
		Event e;

		JSONObject cJson = new JSONObject();
		cJson.put("condition", "is_critical_hit");

		JSONObject sJson = new JSONObject();
		sJson.put("subevent", "attack_roll");
		sJson.put("attack_ability", "str");
		sJson.put("hit", new JSONArray());
		sJson.put("miss", new JSONArray());

		JSONObject oJson = new JSONObject();
		JSONObject abilityScores = new JSONObject();
		abilityScores.put("str", 10L);
		abilityScores.put("dex", 10L);
		oJson.put("ability_scores", abilityScores);
		oJson.put("effects", new JSONArray());

		JSONObject eJson = new JSONObject();
		eJson.put("tags", new JSONArray());

		c = new IsCriticalHit();
		s = new AttackRoll();
		o = new GameObject(oJson);
		e = new Event(eJson);

		try {
			Die.enableDiceControl(new long[] { 20L });
			s.parse(sJson, e, o, o);
			assertTrue(c.parse(cJson, null, s));
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		} catch (ConditionMismatchException ex) {
			ex.printStackTrace();
			fail("Condition mismatch");
		}
	}

	@Test
	@DisplayName("IsCriticalMiss")
	@SuppressWarnings("unchecked")
	void test003() {
		Condition c;
		AttackRoll s;
		GameObject o;
		Event e;

		JSONObject cJson = new JSONObject();
		cJson.put("condition", "is_critical_miss");

		JSONObject sJson = new JSONObject();
		sJson.put("subevent", "attack_roll");
		sJson.put("attack_ability", "str");
		sJson.put("hit", new JSONArray());
		sJson.put("miss", new JSONArray());

		JSONObject oJson = new JSONObject();
		JSONObject abilityScores = new JSONObject();
		abilityScores.put("str", 10L);
		abilityScores.put("dex", 10L);
		oJson.put("ability_scores", abilityScores);
		oJson.put("effects", new JSONArray());

		JSONObject eJson = new JSONObject();
		eJson.put("tags", new JSONArray());

		c = new IsCriticalMiss();
		s = new AttackRoll();
		o = new GameObject(oJson);
		e = new Event(eJson);

		try {
			Die.enableDiceControl(new long[] { 1L });
			s.parse(sJson, e, o, o);
			assertTrue(c.parse(cJson, null, s));
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		} catch (ConditionMismatchException ex) {
			ex.printStackTrace();
			fail("Condition mismatch");
		}
	}

	@Test
	@DisplayName("IsRollBelow")
	@SuppressWarnings("unchecked")
	void test004() {
		Condition c;
		AttackRoll s;
		GameObject o;
		Event e;

		JSONObject cJson = new JSONObject();
		cJson.put("condition", "is_roll_below");
		cJson.put("value", 10L);

		JSONObject sJson = new JSONObject();
		sJson.put("subevent", "attack_roll");
		sJson.put("attack_ability", "str");
		sJson.put("hit", new JSONArray());
		sJson.put("miss", new JSONArray());

		JSONObject oJson = new JSONObject();
		JSONObject abilityScores = new JSONObject();
		abilityScores.put("str", 10L);
		abilityScores.put("dex", 10L);
		oJson.put("ability_scores", abilityScores);
		oJson.put("effects", new JSONArray());

		JSONObject eJson = new JSONObject();
		eJson.put("tags", new JSONArray());

		c = new IsRollBelow();
		s = new AttackRoll();
		o = new GameObject(oJson);
		e = new Event(eJson);

		try {
			Die.enableDiceControl(new long[] { 5L });
			s.parse(sJson, e, o, o);
			assertTrue(c.parse(cJson, null, s));

			Die.enableDiceControl(new long[] { 15L });
			s.parse(sJson, e, o, o);
			assertFalse(c.parse(cJson, null, s));
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		} catch (ConditionMismatchException ex) {
			ex.printStackTrace();
			fail("Condition mismatch");
		}
	}

	@Test
	@DisplayName("IsRollAbove")
	@SuppressWarnings("unchecked")
	void test005() {
		Condition c;
		AttackRoll s;
		GameObject o;
		Event e;

		JSONObject cJson = new JSONObject();
		cJson.put("condition", "is_roll_above");
		cJson.put("value", 10L);

		JSONObject sJson = new JSONObject();
		sJson.put("subevent", "attack_roll");
		sJson.put("attack_ability", "str");
		sJson.put("hit", new JSONArray());
		sJson.put("miss", new JSONArray());

		JSONObject oJson = new JSONObject();
		JSONObject abilityScores = new JSONObject();
		abilityScores.put("str", 10L);
		abilityScores.put("dex", 10L);
		oJson.put("ability_scores", abilityScores);
		oJson.put("effects", new JSONArray());

		JSONObject eJson = new JSONObject();
		eJson.put("tags", new JSONArray());

		c = new IsRollAbove();
		s = new AttackRoll();
		o = new GameObject(oJson);
		e = new Event(eJson);

		try {
			Die.enableDiceControl(new long[] { 5L });
			s.parse(sJson, e, o, o);
			assertFalse(c.parse(cJson, null, s));

			Die.enableDiceControl(new long[] { 15L });
			s.parse(sJson, e, o, o);
			assertTrue(c.parse(cJson, null, s));
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		} catch (ConditionMismatchException ex) {
			ex.printStackTrace();
			fail("Condition mismatch");
		}
	}

	@Test
	@DisplayName("DealsDamageType")
	@SuppressWarnings("unchecked")
	void test006() {
		Condition c;
		GameObject o;
		Subevent s;
		Event e;

		JSONObject cJson = new JSONObject();
		cJson.put("condition", "deals_damage_type");
		cJson.put("damage_type", "cold");

		JSONObject oJson = new JSONObject();
		oJson.put("effects", new JSONArray());

		JSONObject sJson = new JSONObject();
		sJson.put("subevent", "damage_calculation");

		JSONObject eJson = new JSONObject();
		JSONArray damageList = new JSONArray();
		JSONObject damageElement = new JSONObject();
		damageElement.put("dice", 1L);
		damageElement.put("size", 1L);
		damageElement.put("damage_type", "cold");
		damageList.add(damageElement);
		eJson.put("damage", damageList);
		eJson.put("subevents", new JSONArray());

		o = new GameObject(oJson);
		e = new Event(eJson);

		try {
			e.invoke(new Vector(), new Vector(), o);
			s = new DamageCalculation();
			s.parse(sJson, e, o, o);
			c = new DealsDamageType();
			assertTrue(c.parse(cJson, null, s));
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		} catch (ConditionMismatchException ex) {
			ex.printStackTrace();
			fail("Condition mismatch");
		}
	}

}
