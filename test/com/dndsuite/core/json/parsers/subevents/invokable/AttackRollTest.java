package com.dndsuite.core.json.parsers.subevents.invokable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.dndsuite.core.Event;
import com.dndsuite.core.GameObject;
import com.dndsuite.exceptions.SubeventMismatchException;
import com.dndsuite.maths.dice.Die;

class AttackRollTest {

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
	}

	@Test
	@DisplayName("Normal roll")
	void test001() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString;

			// set up parent Event
			jsonString = "{\"tags\":[]}";
			JSONObject eventJson = (JSONObject) parser.parse(jsonString);
			Event parentEvent = new Event(eventJson);

			// set up dummy GameObject actor
			jsonString = "{\"ability_scores\":{\"str\":12,\"dex\":12,\"con\":12,\"int\":12,\"wis\":12,\"cha\":12},\"effects\":[]}";
			JSONObject dummyJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(dummyJson);

			// set up AttackRoll subevent
			jsonString = "{\"subevent\":\"attack_roll\",\"attack_ability\":\"str\",\"hit\":[],\"miss\":[]}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			AttackRoll attackRoll = new AttackRoll();

			long roll = 10L;

			// check value of attack roll
			Die.enableDiceControl(new long[] { roll });
			attackRoll.parse(subeventJson, parentEvent, dummy, dummy);
			assertEquals(roll + 1L, attackRoll.get()); // +1L comes from str modifier

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		}
	}

	@Test
	@DisplayName("Roll with advantage")
	void test002() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString;

			// set up parent Event
			jsonString = "{\"tags\":[]}";
			JSONObject eventJson = (JSONObject) parser.parse(jsonString);
			Event parentEvent = new Event(eventJson);

			// set up dummy GameObject actor
			jsonString = "{\"ability_scores\":{\"str\":10,\"dex\":10,\"con\":10,\"int\":10,\"wis\":10,\"cha\":10},\"effects\":[]}";
			JSONObject dummyJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(dummyJson);

			// set up AttackRoll subevent
			jsonString = "{\"subevent\":\"attack_roll\",\"attack_ability\":\"str\",\"hit\":[],\"miss\":[]}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			AttackRoll attackRoll = new AttackRoll();

			// grant advantage
			attackRoll.addTag("advantage");
			long hiRoll = 15L;
			long loRoll = 5L;

			// check value of attack roll (case 1)
			Die.enableDiceControl(new long[] { loRoll, hiRoll });
			attackRoll.parse(subeventJson, parentEvent, dummy, dummy);
			assertEquals(hiRoll, attackRoll.get());

			// check value of attack roll (case 2)
			Die.enableDiceControl(new long[] { hiRoll, loRoll });
			attackRoll.parse(subeventJson, parentEvent, dummy, dummy);
			assertEquals(hiRoll, attackRoll.get());

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		}
	}

	@Test
	@DisplayName("Roll with disadvantage")
	void test003() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString;

			// set up parent Event
			jsonString = "{\"tags\":[]}";
			JSONObject eventJson = (JSONObject) parser.parse(jsonString);
			Event parentEvent = new Event(eventJson);

			// set up dummy GameObject actor
			jsonString = "{\"ability_scores\":{\"str\":10,\"dex\":10,\"con\":10,\"int\":10,\"wis\":10,\"cha\":10},\"effects\":[]}";
			JSONObject dummyJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(dummyJson);

			// set up AttackRoll subevent
			jsonString = "{\"subevent\":\"attack_roll\",\"attack_ability\":\"str\",\"hit\":[],\"miss\":[]}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			AttackRoll attackRoll = new AttackRoll();

			// grant advantage
			attackRoll.addTag("disadvantage");
			long hiRoll = 15L;
			long loRoll = 5L;

			// check value of attack roll (case 1)
			Die.enableDiceControl(new long[] { loRoll, hiRoll });
			attackRoll.parse(subeventJson, parentEvent, dummy, dummy);
			assertEquals(loRoll, attackRoll.get());

			// check value of attack roll (case 2)
			Die.enableDiceControl(new long[] { hiRoll, loRoll });
			attackRoll.parse(subeventJson, parentEvent, dummy, dummy);
			assertEquals(loRoll, attackRoll.get());

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		}
	}

	@Test
	@DisplayName("Roll with bonuses")
	void test004() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString;

			// set up parent Event
			jsonString = "{\"tags\":[]}";
			JSONObject eventJson = (JSONObject) parser.parse(jsonString);
			Event parentEvent = new Event(eventJson);

			// set up dummy GameObject actor
			jsonString = "{\"ability_scores\":{\"str\":10,\"dex\":10,\"con\":10,\"int\":10,\"wis\":10,\"cha\":10},\"effects\":[]}";
			JSONObject dummyJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(dummyJson);

			// set up AttackRoll subevent
			jsonString = "{\"subevent\":\"attack_roll\",\"attack_ability\":\"str\",\"hit\":[],\"miss\":[]}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			AttackRoll attackRoll = new AttackRoll();

			long roll = 10L;
			long bonus = 2L;
			attackRoll.addBonus(bonus);

			// check value of attack roll
			Die.enableDiceControl(new long[] { roll });
			attackRoll.parse(subeventJson, parentEvent, dummy, dummy);
			assertEquals(roll + bonus, attackRoll.get());

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		}
	}

	@Test
	@DisplayName("Roll with set values")
	void test005() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString;

			// set up parent Event
			jsonString = "{\"tags\":[]}";
			JSONObject eventJson = (JSONObject) parser.parse(jsonString);
			Event parentEvent = new Event(eventJson);

			// set up dummy GameObject actor
			jsonString = "{\"ability_scores\":{\"str\":10,\"dex\":10,\"con\":10,\"int\":10,\"wis\":10,\"cha\":10},\"effects\":[]}";
			JSONObject dummyJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(dummyJson);

			// set up AttackRoll subevent
			jsonString = "{\"subevent\":\"attack_roll\",\"attack_ability\":\"str\",\"hit\":[],\"miss\":[]}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			AttackRoll attackRoll = new AttackRoll();

			long roll = 10L;
			long set = 5L;
			attackRoll.setTo(set);

			// check value of attack roll
			Die.enableDiceControl(new long[] { roll });
			attackRoll.parse(subeventJson, parentEvent, dummy, dummy);
			assertEquals(set, attackRoll.get());

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		}
	}

	@Test
	@DisplayName("Roll with set values and bonus")
	void test006() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString;

			// set up parent Event
			jsonString = "{\"tags\":[]}";
			JSONObject eventJson = (JSONObject) parser.parse(jsonString);
			Event parentEvent = new Event(eventJson);

			// set up dummy GameObject actor
			jsonString = "{\"ability_scores\":{\"str\":10,\"dex\":10,\"con\":10,\"int\":10,\"wis\":10,\"cha\":10},\"effects\":[]}";
			JSONObject dummyJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(dummyJson);

			// set up AttackRoll subevent
			jsonString = "{\"subevent\":\"attack_roll\",\"attack_ability\":\"str\",\"hit\":[],\"miss\":[]}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			AttackRoll attackRoll = new AttackRoll();

			long roll = 10L;
			long set = 5L;
			long bonus = 2L;
			attackRoll.setTo(set);
			attackRoll.addBonus(bonus);

			// check value of attack roll
			Die.enableDiceControl(new long[] { roll });
			attackRoll.parse(subeventJson, parentEvent, dummy, dummy);
			assertEquals(set + bonus, attackRoll.get());

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		}
	}

	@Test
	@DisplayName("Critical miss")
	void test007() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString;

			// set up parent Event
			jsonString = "{\"tags\":[]}";
			JSONObject eventJson = (JSONObject) parser.parse(jsonString);
			Event parentEvent = new Event(eventJson);

			// set up dummy GameObject actor
			jsonString = "{\"ability_scores\":{\"str\":10,\"dex\":10,\"con\":10,\"int\":10,\"wis\":10,\"cha\":10},\"effects\":[]}";
			JSONObject dummyJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(dummyJson);

			// set up AttackRoll subevent
			jsonString = "{\"subevent\":\"attack_roll\",\"attack_ability\":\"str\",\"hit\":[],\"miss\":[]}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			AttackRoll attackRoll = new AttackRoll();

			long roll = 1L;

			// check value of attack roll
			Die.enableDiceControl(new long[] { roll });
			attackRoll.parse(subeventJson, parentEvent, dummy, dummy);
			assertTrue(parentEvent.hasTag("critical_miss"));

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		}
	}

	@Test
	@DisplayName("Critical hit")
	void test008() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString;

			// set up parent Event
			jsonString = "{\"tags\":[]}";
			JSONObject eventJson = (JSONObject) parser.parse(jsonString);
			Event parentEvent = new Event(eventJson);

			// set up dummy GameObject actor
			jsonString = "{\"ability_scores\":{\"str\":10,\"dex\":10,\"con\":10,\"int\":10,\"wis\":10,\"cha\":10},\"effects\":[]}";
			JSONObject dummyJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(dummyJson);

			// set up AttackRoll subevent
			jsonString = "{\"subevent\":\"attack_roll\",\"attack_ability\":\"str\",\"hit\":[],\"miss\":[]}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			AttackRoll attackRoll = new AttackRoll();

			long roll = 20L;

			// check value of attack roll
			Die.enableDiceControl(new long[] { roll });
			attackRoll.parse(subeventJson, parentEvent, dummy, dummy);
			assertTrue(parentEvent.hasTag("critical_hit"));

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		}
	}

	@Test
	@DisplayName("Critical hit (lower critical threshold)")
	void test009() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString;

			// set up parent Event
			jsonString = "{\"tags\":[]}";
			JSONObject eventJson = (JSONObject) parser.parse(jsonString);
			Event parentEvent = new Event(eventJson);

			// set up dummy GameObject actor
			jsonString = "{\"ability_scores\":{\"str\":10,\"dex\":10,\"con\":10,\"int\":10,\"wis\":10,\"cha\":10},\"effects\":[]}";
			JSONObject dummyJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(dummyJson);

			// set up AttackRoll subevent
			jsonString = "{\"subevent\":\"attack_roll\",\"attack_ability\":\"str\",\"hit\":[],\"miss\":[],\"critical_threshold\":19}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			AttackRoll attackRoll = new AttackRoll();

			long roll = 19L;

			// check value of attack roll
			Die.enableDiceControl(new long[] { roll });
			attackRoll.parse(subeventJson, parentEvent, dummy, dummy);
			assertTrue(parentEvent.hasTag("critical_hit"));

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		}
	}

}
