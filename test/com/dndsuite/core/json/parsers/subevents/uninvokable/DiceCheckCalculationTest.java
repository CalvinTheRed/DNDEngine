package com.dndsuite.core.json.parsers.subevents.uninvokable;

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
import com.dndsuite.maths.Vector;

class DiceCheckCalculationTest {

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
	@DisplayName("Normal dice check calculation")
	void test001() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString;

			// set up dummy GameObject actor
			jsonString = "{\"ability_scores\":{\"str\": 12,\"dex\":12,\"con\":12,\"int\":12,\"wis\":12,\"cha\":12},\"level\":1,\"effects\":[]}";
			JSONObject dummyJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(dummyJson);

			// set up parent Event
			jsonString = "{\"tags\":[],\"subevents\":[]}";
			JSONObject eventJson = (JSONObject) parser.parse(jsonString);
			Event parentEvent = new Event(eventJson);
			parentEvent.invoke(new Vector(), new Vector(), dummy);

			// set up DiceCheckCalculation subevent
			jsonString = "{\"subevent\":\"dice_check_calculation\",\"dc_ability\":\"wis\"}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			DiceCheckCalculation diceCheckCalculation = new DiceCheckCalculation();

			// verify dice check calculation
			diceCheckCalculation.parse(subeventJson, parentEvent, dummy, dummy);
			assertEquals(8L + 2L + 1L, diceCheckCalculation.get());

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
	@DisplayName("Calculation with bonus")
	void test002() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString;

			// set up dummy GameObject actor
			jsonString = "{\"ability_scores\":{\"str\": 12,\"dex\":12,\"con\":12,\"int\":12,\"wis\":12,\"cha\":12},\"level\":1,\"effects\":[]}";
			JSONObject dummyJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(dummyJson);

			// set up parent Event
			jsonString = "{\"tags\":[],\"subevents\":[]}";
			JSONObject eventJson = (JSONObject) parser.parse(jsonString);
			Event parentEvent = new Event(eventJson);
			parentEvent.invoke(new Vector(), new Vector(), dummy);

			// set up DiceCheckCalculation subevent
			jsonString = "{\"subevent\":\"dice_check_calculation\",\"dc_ability\":\"wis\"}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			DiceCheckCalculation diceCheckCalculation = new DiceCheckCalculation();

			long bonus = 2L;
			diceCheckCalculation.addBonus(bonus);

			// verify dice check calculation
			diceCheckCalculation.parse(subeventJson, parentEvent, dummy, dummy);
			assertEquals(8L + 2L + 1L + bonus, diceCheckCalculation.get());

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
	@DisplayName("Calculation with set value")
	void test003() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString;

			// set up dummy GameObject actor
			jsonString = "{\"ability_scores\":{\"str\": 12,\"dex\":12,\"con\":12,\"int\":12,\"wis\":12,\"cha\":12},\"level\":1,\"effects\":[]}";
			JSONObject dummyJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(dummyJson);

			// set up parent Event
			jsonString = "{\"tags\":[],\"subevents\":[]}";
			JSONObject eventJson = (JSONObject) parser.parse(jsonString);
			Event parentEvent = new Event(eventJson);
			parentEvent.invoke(new Vector(), new Vector(), dummy);

			// set up DiceCheckCalculation subevent
			jsonString = "{\"subevent\":\"dice_check_calculation\",\"dc_ability\":\"wis\"}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			DiceCheckCalculation diceCheckCalculation = new DiceCheckCalculation();

			long set = 15L;
			diceCheckCalculation.setTo(set);
			// 5L < 15L so lower set should not overwrite 15L
			diceCheckCalculation.setTo(5L);

			// verify dice check calculation
			diceCheckCalculation.parse(subeventJson, parentEvent, dummy, dummy);
			assertEquals(set, diceCheckCalculation.get());

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
	@DisplayName("Calculation with set value and bonus")
	void test004() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString;

			// set up dummy GameObject actor
			jsonString = "{\"ability_scores\":{\"str\": 12,\"dex\":12,\"con\":12,\"int\":12,\"wis\":12,\"cha\":12},\"level\":1,\"effects\":[]}";
			JSONObject dummyJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(dummyJson);

			// set up parent Event
			jsonString = "{\"tags\":[],\"subevents\":[]}";
			JSONObject eventJson = (JSONObject) parser.parse(jsonString);
			Event parentEvent = new Event(eventJson);
			parentEvent.invoke(new Vector(), new Vector(), dummy);

			// set up DiceCheckCalculation subevent
			jsonString = "{\"subevent\":\"dice_check_calculation\",\"dc_ability\":\"wis\"}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			DiceCheckCalculation diceCheckCalculation = new DiceCheckCalculation();

			long set = 15L;
			long bonus = 2L;
			diceCheckCalculation.setTo(set);
			diceCheckCalculation.addBonus(bonus);

			// verify dice check calculation
			diceCheckCalculation.parse(subeventJson, parentEvent, dummy, dummy);
			assertEquals(set, diceCheckCalculation.get());

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
