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
import com.dndsuite.exceptions.SubeventMismatchException;
import com.dndsuite.maths.Vector;
import com.dndsuite.maths.dice.Die;

class DamageTest {

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
	@DisplayName("Single damage instance")
	void test001() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString;

			// set up dummy GameObject actor
			jsonString = "{\"health\":{\"max\":10,\"base\":10,\"current\":10,\"tmp\":0},\"effects\":[]}";
			JSONObject dummyJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(dummyJson);

			// set up parent Event
			long damageValue = 6L;
			Die.enableDiceControl(new long[] { damageValue });
			jsonString = "{\"tags\":[],\"damage\":[{\"dice\":1,\"size\":6,\"damage_type\":\"cold\"}],\"subevents\":[]}";
			JSONObject eventJson = (JSONObject) parser.parse(jsonString);
			Event parentEvent = new Event(eventJson);
			parentEvent.invoke(new Vector(), new Vector(), dummy);

			// set up Damage subevent
			jsonString = "{\"subevent\":\"damage\"}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			Damage damage = new Damage();

			// verify damage dealt
			damage.parse(subeventJson, parentEvent, dummy, dummy);
			assertEquals(10L - damageValue, dummy.getHealth().get("current"));

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		}
	}

	@Test
	@DisplayName("Multiple damage istances w/ shared damage")
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

			// set up Damage subevent
			jsonString = "{\"subevent\":\"damage\"}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			Damage damage = new Damage();

			// verify damage dealt
			damage.parse(subeventJson, parentEvent, dummy, dummy);
			damage.parse(subeventJson, parentEvent, dummy, dummy);
			assertEquals(10L - 2 * damageValue, dummy.getHealth().get("current"));

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		}
	}

}
