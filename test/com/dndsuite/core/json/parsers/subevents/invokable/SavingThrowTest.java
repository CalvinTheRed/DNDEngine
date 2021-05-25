package com.dndsuite.core.json.parsers.subevents.invokable;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import com.dndsuite.core.UUIDTable;
import com.dndsuite.core.VirtualBoard;
import com.dndsuite.exceptions.JSONFormatException;
import com.dndsuite.exceptions.SubeventMismatchException;
import com.dndsuite.maths.dice.Die;

class SavingThrowTest {

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
		VirtualBoard.clear();
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

			// set up SavingThrow subevent
			jsonString = "{\"subevent\":\"saving_throw\",\"save_ability\":\"dex\",\"dc_ability\":\"wis\",\"pass\":[],\"fail\":[]}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			SavingThrow savingThrow = new SavingThrow();

			long roll = 10L;

			// check value of saving throw
			Die.enableDiceControl(new long[] { roll });
			savingThrow.parse(subeventJson, parentEvent, dummy, dummy);
			assertEquals(roll + 1L, savingThrow.get()); // +1L comes from dex modifier

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		} catch (JSONFormatException ex) {
			ex.printStackTrace();
			fail("JSON format exception");
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

			// set up SavingThrow subevent
			jsonString = "{\"subevent\":\"saving_throw\",\"save_ability\":\"dex\",\"dc_ability\":\"wis\",\"pass\":[],\"fail\":[]}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			SavingThrow savingThrow = new SavingThrow();

			// grant advantage
			savingThrow.addTag("advantage");
			long hiRoll = 15L;
			long loRoll = 5L;

			// check value of saving throw (case 1)
			Die.enableDiceControl(new long[] { loRoll, hiRoll });
			savingThrow.parse(subeventJson, parentEvent, dummy, dummy);
			assertEquals(hiRoll, savingThrow.get());

			// check value of saving throw (case 2)
			Die.enableDiceControl(new long[] { hiRoll, loRoll });
			savingThrow.parse(subeventJson, parentEvent, dummy, dummy);
			assertEquals(hiRoll, savingThrow.get());

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		} catch (JSONFormatException ex) {
			ex.printStackTrace();
			fail("JSON format exception");
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

			// set up SavingThrow subevent
			jsonString = "{\"subevent\":\"saving_throw\",\"save_ability\":\"dex\",\"dc_ability\":\"wis\",\"pass\":[],\"fail\":[]}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			SavingThrow savingThrow = new SavingThrow();

			// grant disadvantage
			savingThrow.addTag("disadvantage");
			long hiRoll = 15L;
			long loRoll = 5L;

			// check value of saving throw (case 1)
			Die.enableDiceControl(new long[] { loRoll, hiRoll });
			savingThrow.parse(subeventJson, parentEvent, dummy, dummy);
			assertEquals(loRoll, savingThrow.get());

			// check value of saving throw (case 2)
			Die.enableDiceControl(new long[] { hiRoll, loRoll });
			savingThrow.parse(subeventJson, parentEvent, dummy, dummy);
			assertEquals(loRoll, savingThrow.get());

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		} catch (JSONFormatException ex) {
			ex.printStackTrace();
			fail("JSON format exception");
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

			// set up SavingThrow subevent
			jsonString = "{\"subevent\":\"saving_throw\",\"save_ability\":\"dex\",\"dc_ability\":\"wis\",\"pass\":[],\"fail\":[]}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			SavingThrow savingThrow = new SavingThrow();

			long roll = 10L;
			long bonus = 2L;
			savingThrow.addBonus(bonus);

			// check value of saving throw
			Die.enableDiceControl(new long[] { roll });
			savingThrow.parse(subeventJson, parentEvent, dummy, dummy);
			assertEquals(roll + bonus, savingThrow.get());

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		} catch (JSONFormatException ex) {
			ex.printStackTrace();
			fail("JSON format exception");
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

			// set up SavingThrow subevent
			jsonString = "{\"subevent\":\"saving_throw\",\"save_ability\":\"dex\",\"dc_ability\":\"wis\",\"pass\":[],\"fail\":[]}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			SavingThrow savingThrow = new SavingThrow();

			long roll = 10L;
			long set = 5L;
			savingThrow.setTo(set);

			// check value of saving throw
			Die.enableDiceControl(new long[] { roll });
			savingThrow.parse(subeventJson, parentEvent, dummy, dummy);
			assertEquals(set, savingThrow.get());

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		} catch (JSONFormatException ex) {
			ex.printStackTrace();
			fail("JSON format exception");
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

			// set up SavingThrow subevent
			jsonString = "{\"subevent\":\"saving_throw\",\"save_ability\":\"dex\",\"dc_ability\":\"wis\",\"pass\":[],\"fail\":[]}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			SavingThrow savingThrow = new SavingThrow();

			long roll = 10L;
			long set = 5L;
			long bonus = 2L;
			savingThrow.setTo(set);
			savingThrow.addBonus(bonus);

			// check value of saving throw
			Die.enableDiceControl(new long[] { roll });
			savingThrow.parse(subeventJson, parentEvent, dummy, dummy);
			assertEquals(set + bonus, savingThrow.get());

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		} catch (JSONFormatException ex) {
			ex.printStackTrace();
			fail("JSON format exception");
		}
	}

}
