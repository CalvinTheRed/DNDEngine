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

class ArmorClassCalculationTest {

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
	@DisplayName("Default armor class formula")
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

			// set up ArmorClassCalculation subevent
			jsonString = "{\"subevent\":\"armor_class_calculation\"}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			ArmorClassCalculation armorClassCalculation = new ArmorClassCalculation();

			// check calculated armor class
			armorClassCalculation.parse(subeventJson, parentEvent, dummy, dummy);
			assertEquals(10L + 1L, armorClassCalculation.get());

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
	@DisplayName("Armor class with bonus")
	void test002() {
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

			// set up ArmorClassCalculation subevent
			jsonString = "{\"subevent\":\"armor_class_calculation\"}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			ArmorClassCalculation armorClassCalculation = new ArmorClassCalculation();

			long bonus = 2L;
			armorClassCalculation.addBonus(bonus);

			// check calculated armor class
			armorClassCalculation.parse(subeventJson, parentEvent, dummy, dummy);
			assertEquals(10L + 1L + bonus, armorClassCalculation.get());

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
	@DisplayName("Armor class with set value")
	void test003() {
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

			// set up ArmorClassCalculation subevent
			jsonString = "{\"subevent\":\"armor_class_calculation\"}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			ArmorClassCalculation armorClassCalculation = new ArmorClassCalculation();

			long set = 15L;
			armorClassCalculation.setTo(set);

			// check calculated armor class
			armorClassCalculation.parse(subeventJson, parentEvent, dummy, dummy);
			assertEquals(set, armorClassCalculation.get());

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
	@DisplayName("Armor class with set value and bonus")
	void test004() {
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

			// set up ArmorClassCalculation subevent
			jsonString = "{\"subevent\":\"armor_class_calculation\"}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			ArmorClassCalculation armorClassCalculation = new ArmorClassCalculation();

			long set = 15L;
			long bonus = 2L;
			armorClassCalculation.setTo(set);
			armorClassCalculation.addBonus(bonus);

			// check calculated armor class
			armorClassCalculation.parse(subeventJson, parentEvent, dummy, dummy);
			assertEquals(set + bonus, armorClassCalculation.get());

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
