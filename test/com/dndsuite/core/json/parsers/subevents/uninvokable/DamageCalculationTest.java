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
import com.dndsuite.exceptions.SubeventMismatchException;
import com.dndsuite.maths.Vector;
import com.dndsuite.maths.dice.Die;

class DamageCalculationTest {

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
	@DisplayName("Apply damage bonus to contained damage type")
	void test001() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString;

			// set up dummy GameObject actor
			jsonString = "{\"health\":{\"max\":10,\"base\":10,\"current\":10,\"tmp\":0},\"effects\":[]}";
			JSONObject dummyJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(dummyJson);

			// set up parent Event
			long damageValue = 2L;
			Die.enableDiceControl(new long[] { damageValue });
			jsonString = "{\"tags\":[],\"damage\":[{\"dice\":1,\"size\":6,\"damage_type\":\"cold\"}],\"subevents\":[]}";
			JSONObject eventJson = (JSONObject) parser.parse(jsonString);
			Event parentEvent = new Event(eventJson);
			parentEvent.invoke(new Vector(), new Vector(), dummy);

			// set up DamageCalculation subevent
			jsonString = "{\"subevent\":\"damage_calculation\"}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			DamageCalculation damageCalculation = new DamageCalculation();
			damageCalculation.setDamageDice(parentEvent.getBaseDiceCollection());

			long bonus = 2L;
			damageCalculation.addDamageBonus(bonus, "cold");

			// verify addition of damage bonus
			assertEquals(1, damageCalculation.getDamageDice().size());
			assertEquals(bonus, damageCalculation.getDamageDice().get(0).getBonus());

			// verify damage dealt
			damageCalculation.parse(subeventJson, parentEvent, dummy, dummy);
			assertEquals(10L - damageValue - bonus, dummy.getHealth().get("current"));

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		}
	}

	@Test
	@DisplayName("Apply damage bonus to novel damage type")
	void test002() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString;

			// set up dummy GameObject actor
			jsonString = "{\"health\":{\"max\":10,\"base\":10,\"current\":10,\"tmp\":0},\"effects\":[]}";
			JSONObject dummyJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(dummyJson);

			// set up parent Event
			long damageValue = 2L;
			Die.enableDiceControl(new long[] { damageValue });
			jsonString = "{\"tags\":[],\"damage\":[{\"dice\":1,\"size\":6,\"damage_type\":\"cold\"}],\"subevents\":[]}";
			JSONObject eventJson = (JSONObject) parser.parse(jsonString);
			Event parentEvent = new Event(eventJson);
			parentEvent.invoke(new Vector(), new Vector(), dummy);

			// set up DamageCalculation subevent
			jsonString = "{\"subevent\":\"damage_calculation\"}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			DamageCalculation damageCalculation = new DamageCalculation();
			damageCalculation.setDamageDice(parentEvent.getBaseDiceCollection());

			long bonus = 2L;
			damageCalculation.addDamageBonus(bonus, "fire");

			// verify addition of damage bonus
			assertEquals(2, damageCalculation.getDamageDice().size());
			assertEquals(bonus, damageCalculation.getDamageDice().get(1).getBonus());

			// verify damage dealt
			damageCalculation.parse(subeventJson, parentEvent, dummy, dummy);
			assertEquals(10L - damageValue - bonus, dummy.getHealth().get("current"));

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		}
	}

}
