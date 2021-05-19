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
import com.dndsuite.exceptions.SubeventMismatchException;

class AbilityScoreCalculationTest {

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
	@DisplayName("Normal calculation")
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

			// set up AbilityScoreCalculation subevent
			jsonString = "{\"subevent\":\"ability_score_calculation\",\"ability\":\"str\"}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			AbilityScoreCalculation abilityScoreCalculation = new AbilityScoreCalculation();

			// check calculated ability score
			abilityScoreCalculation.parse(subeventJson, parentEvent, dummy, dummy);
			assertEquals(12L, abilityScoreCalculation.get());

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		}
	}

	@Test
	@DisplayName("Calculation with bonus")
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

			// set up AbilityScoreCalculation subevent
			jsonString = "{\"subevent\":\"ability_score_calculation\",\"ability\":\"str\"}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			AbilityScoreCalculation abilityScoreCalculation = new AbilityScoreCalculation();

			long bonus = 2L;
			abilityScoreCalculation.addBonus(bonus);

			// check calculated ability score
			abilityScoreCalculation.parse(subeventJson, parentEvent, dummy, dummy);
			assertEquals(12L + bonus, abilityScoreCalculation.get());

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		}
	}

	@Test
	@DisplayName("Calculation with set value")
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

			// set up AbilityScoreCalculation subevent
			jsonString = "{\"subevent\":\"ability_score_calculation\",\"ability\":\"str\"}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			AbilityScoreCalculation abilityScoreCalculation = new AbilityScoreCalculation();

			long set = 10L;
			abilityScoreCalculation.setTo(set);
			// 5L < 10L, so second set should not overwrite the first
			abilityScoreCalculation.setTo(5L);

			// check calculated ability score
			abilityScoreCalculation.parse(subeventJson, parentEvent, dummy, dummy);
			assertEquals(set, abilityScoreCalculation.get());

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		}
	}

	@Test
	@DisplayName("Calculation with set value and bonus")
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

			// set up AbilityScoreCalculation subevent
			jsonString = "{\"subevent\":\"ability_score_calculation\",\"ability\":\"str\"}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			AbilityScoreCalculation abilityScoreCalculation = new AbilityScoreCalculation();

			long set = 10L;
			long bonus = 5L;
			abilityScoreCalculation.setTo(set);
			// bonus does not get included with a set ability score calculation
			abilityScoreCalculation.addBonus(bonus);

			// check calculated ability score
			abilityScoreCalculation.parse(subeventJson, parentEvent, dummy, dummy);
			assertEquals(set, abilityScoreCalculation.get());

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		}
	}

}
