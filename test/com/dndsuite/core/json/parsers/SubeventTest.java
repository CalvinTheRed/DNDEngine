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

import com.dndsuite.core.Event;
import com.dndsuite.core.GameObject;
import com.dndsuite.core.Item;
import com.dndsuite.core.UUIDTable;
import com.dndsuite.core.VirtualBoard;
import com.dndsuite.core.json.parsers.subevents.AbilityScoreCalculation;
import com.dndsuite.core.json.parsers.subevents.ArmorClassCalculation;
import com.dndsuite.core.json.parsers.subevents.AttackRoll;
import com.dndsuite.core.json.parsers.subevents.Damage;
import com.dndsuite.core.json.parsers.subevents.DamageCalculation;
import com.dndsuite.core.json.parsers.subevents.DamageDiceCollection;
import com.dndsuite.core.json.parsers.subevents.DiceCheckCalculation;
import com.dndsuite.core.json.parsers.subevents.SavingThrow;
import com.dndsuite.core.json.parsers.subevents.UnequipItem;
import com.dndsuite.exceptions.JSONFormatException;
import com.dndsuite.exceptions.SubeventMismatchException;
import com.dndsuite.maths.Vector;
import com.dndsuite.maths.dice.DamageDiceGroup;
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
	@DisplayName("ApplyEffect")
	@SuppressWarnings("unchecked")
	void test002() {
		fail("not yet implemented");
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

	@Test
	@DisplayName("Damage")
	@SuppressWarnings("unchecked")
	void test005() {
		Damage s;
		JSONObject oJson;
		JSONObject eJson;
		JSONObject sJson;
		GameObject o;
		Event e;

		oJson = new JSONObject();
		oJson.put("effects", new JSONArray());
		JSONObject health = new JSONObject();
		health.put("max", 10L);
		health.put("base", 10L);
		health.put("current", 10L);
		health.put("tmp", 0L);
		oJson.put("health", health);
		o = new GameObject(oJson);

		eJson = new JSONObject();
		JSONArray damageList = new JSONArray();
		JSONObject damageElement = new JSONObject();
		damageElement.put("dice", 1L);
		damageElement.put("size", 6L);
		damageElement.put("damage_type", "cold");
		damageList.add(damageElement);
		eJson.put("damage", damageList);
		eJson.put("subevents", new JSONArray());

		sJson = new JSONObject();
		sJson.put("subevent", "damage");

		e = new Event(eJson);

		try {
			// damage dealt correctly
			Die.enableDiceControl(new long[] { 5L });
			s = new Damage();
			e.invoke(new Vector(), new Vector(), o);
			s.parse(sJson, e, o, o);
			assertEquals(5L, (long) o.getHealth().get("current"));
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		}
	}

	@Test
	@DisplayName("DamageCalculation")
	@SuppressWarnings("unchecked")
	void test006() {
		DamageCalculation s;
		JSONObject oJson;
		JSONObject eJson;
		JSONObject sJson;
		GameObject o;
		Event e;

		oJson = new JSONObject();
		oJson.put("effects", new JSONArray());
		JSONObject health = new JSONObject();
		health.put("max", 10L);
		health.put("base", 10L);
		health.put("current", 10L);
		health.put("tmp", 0L);
		oJson.put("health", health);
		o = new GameObject(oJson);

		eJson = new JSONObject();
		JSONArray damageList = new JSONArray();
		JSONObject damageElement = new JSONObject();
		damageElement.put("dice", 1L);
		damageElement.put("size", 6L);
		damageElement.put("damage_type", "cold");
		damageList.add(damageElement);
		eJson.put("damage", damageList);
		eJson.put("subevents", new JSONArray());

		sJson = new JSONObject();
		sJson.put("subevent", "damage_calculation");

		e = new Event(eJson);

		try {
			Die.enableDiceControl(new long[] { 1L });
			s = new DamageCalculation();
			e.invoke(new Vector(), new Vector(), o);
			s.setDamageDice(e.getBaseDiceCollection());

			// apply damage bonus to existing damage type
			s.addDamageBonus(2L, "cold");
			assertEquals(1L, s.getDamageDice().size());
			assertEquals(2L, s.getDamageDice().get(0).getBonus());

			// apply damage bonus to new damage type
			s.addDamageBonus(2L, "fire");
			assertEquals(2L, s.getDamageDice().size());
			assertEquals(2L, s.getDamageDice().get(1).getBonus());

			// damage dealt correctly
			s.parse(sJson, e, o, o);
			assertEquals(5L, (long) o.getHealth().get("current"));

		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		}
	}

	@Test
	@DisplayName("DamageDiceCollection")
	@SuppressWarnings("unchecked")
	void test007() {
		DamageDiceCollection s;
		JSONObject oJson;
		JSONObject eJson;
		JSONObject sJson;
		GameObject o;
		Event e;

		oJson = new JSONObject();
		oJson.put("effects", new JSONArray());
		JSONObject health = new JSONObject();
		health.put("max", 10L);
		health.put("base", 10L);
		health.put("current", 10L);
		health.put("tmp", 0L);
		oJson.put("health", health);
		o = new GameObject(oJson);

		eJson = new JSONObject();
		JSONArray damageList = new JSONArray();
		JSONObject damageElement = new JSONObject();
		damageElement.put("dice", 1L);
		damageElement.put("size", 6L);
		damageElement.put("damage_type", "cold");
		damageList.add(damageElement);
		eJson.put("damage", damageList);
		eJson.put("subevents", new JSONArray());

		sJson = new JSONObject();
		sJson.put("subevent", "damage_dice_collection");

		e = new Event(eJson);

		try {
			Die.enableDiceControl(new long[] { 1L, 1L });
			e.invoke(new Vector(), new Vector(), o);
			s = e.getBaseDiceCollection();

			// add damage dice group to damage, with inbuilt bonus
			s.addDamageDiceGroup(new DamageDiceGroup(1L, 6L, 1L, "cold"));
			assertEquals(1, s.getDamageDice().size());
			assertEquals(2, s.getDamageDice().get(0).getDice().size());

			// damage dealt correctly
			s.parse(sJson, e, o, o);
			assertEquals(7L, (long) o.getHealth().get("current"));

		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		}
	}

	@Test
	@DisplayName("DiceCheckCalculation")
	@SuppressWarnings("unchecked")
	void test008() {
		Subevent s = new DiceCheckCalculation();
		JSONObject sJson;
		JSONObject oJson;
		GameObject o;

		oJson = new JSONObject();
		JSONObject abilityScores = new JSONObject();
		abilityScores.put("wis", 12L);
		oJson.put("ability_scores", abilityScores);
		oJson.put("effects", new JSONArray());
		oJson.put("level", 5L);
		o = new GameObject(oJson);

		sJson = new JSONObject();
		sJson.put("subevent", "dice_check_calculation");
		sJson.put("dc_ability", "wis");

		try {
			DiceCheckCalculation dcc = (DiceCheckCalculation) s;
			// Default armor class
			dcc.parse(sJson, null, o, o);
			assertEquals(12L, dcc.get());

			// armor class w/ bonus
			dcc.parse(sJson, null, o, o);
			dcc.addBonus(2L);
			dcc.addBonus(5L);
			assertEquals(19L, dcc.get());

			// armor class w/ set
			dcc.parse(sJson, null, o, o);
			dcc.setTo(13L);
			assertEquals(13L, dcc.get());

			// armor class w/ set & bonus
			dcc.parse(sJson, null, o, o);
			dcc.setTo(13L);
			dcc.addBonus(7L);
			assertEquals(13L, dcc.get());

			// armor class w/ multiple sets
			dcc.parse(sJson, null, o, o);
			dcc.setTo(13L);
			dcc.setTo(7L);
			assertEquals(13L, dcc.get());

		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		}
	}

	@Test
	@DisplayName("InvokeEvent")
	@SuppressWarnings("unchecked")
	void test009() {
		fail("not yet implemented");
	}

	@Test
	@DisplayName("SavingThrow")
	@SuppressWarnings("unchecked")
	void test00A() {
		SavingThrow s;
		JSONObject sJson;
		JSONObject oJson;
		JSONObject eJson;
		GameObject o;
		Event e;

		JSONObject abilityScores = new JSONObject();
		abilityScores.put("str", 10L);
		abilityScores.put("dex", 16L); // +3
		abilityScores.put("con", 10L);
		abilityScores.put("int", 10L);
		abilityScores.put("wis", 12L); // +1
		abilityScores.put("cha", 10L);

		oJson = new JSONObject();
		oJson.put("ability_scores", abilityScores);
		oJson.put("effects", new JSONArray());
		oJson.put("level", 5L);
		o = new GameObject(oJson);

		sJson = new JSONObject();
		sJson.put("subevent", "saving_throw");
		sJson.put("dc_ability", "wis");
		sJson.put("save_ability", "dex");
		sJson.put("pass", new JSONArray());
		sJson.put("fail", new JSONArray());

		try {
			// Normal saving throw
			Die.enableDiceControl(new long[] { 10L });
			s = new SavingThrow();
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			s.parse(sJson, e, o, o);
			assertEquals(13L, s.get());

			// saving throw w/ bonus
			Die.enableDiceControl(new long[] { 10L });
			s = new SavingThrow();
			s.addBonus(2L);
			assertEquals(2L, s.get());
			s.addBonus(7L);
			assertEquals(9L, s.get());
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			s.parse(sJson, e, o, o);
			assertEquals(22L, s.get());

			// saving throw w/ set
			Die.enableDiceControl(new long[] { 10L });
			s = new SavingThrow();
			s.setTo(15L);
			assertEquals(15L, s.get());
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			s.parse(sJson, e, o, o);
			assertEquals(18L, s.get());

			// saving throw w/ set & bonus
			Die.enableDiceControl(new long[] { 10L });
			s = new SavingThrow();
			s.setTo(12L);
			assertEquals(12L, s.get());
			s.addBonus(7L);
			assertEquals(19L, s.get());
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			s.parse(sJson, e, o, o);
			assertEquals(22L, s.get());

			// saving throw w/ multiple sets
			Die.enableDiceControl(new long[] { 10L });
			s = new SavingThrow();
			s.setTo(15L);
			assertEquals(15L, s.get());
			s.setTo(7L);
			assertEquals(7L, s.get());
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			s.parse(sJson, e, o, o);
			assertEquals(10L, s.get());

			// rolling with advantage (1 of 2)
			Die.enableDiceControl(new long[] { 5L, 15L });
			s = new SavingThrow();
			s.addTag("advantage");
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			s.parse(sJson, e, o, o);
			assertEquals(18L, s.get());

			// rolling with advantage (2 of 2)
			Die.enableDiceControl(new long[] { 15L, 5L });
			s = new SavingThrow();
			s.addTag("advantage");
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			s.parse(sJson, e, o, o);
			assertEquals(18L, s.get());

			// rolling with disadvantage (1 of 2)
			Die.enableDiceControl(new long[] { 5L, 15L });
			s = new SavingThrow();
			s.addTag("disadvantage");
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			s.parse(sJson, e, o, o);
			assertEquals(8L, s.get());

			// rolling with disadvantage (2 of 2)
			Die.enableDiceControl(new long[] { 15L, 5L });
			s = new SavingThrow();
			s.addTag("disadvantage");
			eJson = new JSONObject();
			eJson.put("tags", new JSONArray());
			e = new Event(eJson);
			s.parse(sJson, e, o, o);
			assertEquals(8L, s.get());

		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		}
	}

	@Test
	@DisplayName("UnequipItem")
	@SuppressWarnings("unchecked")
	void test00B() {
		UnequipItem s;
		JSONObject sJson;
		JSONObject oJson;
		JSONObject iJson;
		GameObject o;
		Item i;

		oJson = new JSONObject();
		oJson.put("effects", new JSONArray());
		o = new GameObject(oJson);

		long uuid = 1234L;

		sJson = new JSONObject();
		sJson.put("subevent", "unequip_item");
		sJson.put("item_uuid", uuid);

		iJson = new JSONObject();
		iJson.put("tags", new JSONArray());
		iJson.put("uuid", uuid);
		iJson.put("name", "Test Item");

		i = new Item(iJson);
		s = new UnequipItem();
		o = new GameObject(oJson);

		UUIDTable.addToTable(i);

		try {
			// no changes
			s.parse(sJson, null, o, o);
			assertTrue(s.getSuccess());

			// with prevention
			s.parse(sJson, null, o, o);
			s.prevent();
			assertFalse(s.getSuccess());

			// with permission
			s.parse(sJson, null, o, o);
			s.permit();
			assertTrue(s.getSuccess());

			// with prevention and permission
			s.parse(sJson, null, o, o);
			s.prevent();
			s.permit();
			assertTrue(s.getSuccess());

		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		} catch (JSONFormatException ex) {
			ex.printStackTrace();
			fail("JSON format exception");
		}
	}

}
