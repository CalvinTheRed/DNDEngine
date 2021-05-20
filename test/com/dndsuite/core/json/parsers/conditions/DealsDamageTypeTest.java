package com.dndsuite.core.json.parsers.conditions;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
import com.dndsuite.core.UUIDTable;
import com.dndsuite.core.VirtualBoard;
import com.dndsuite.core.json.parsers.Condition;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.core.json.parsers.subevents.invokable.Damage;
import com.dndsuite.exceptions.ConditionMismatchException;
import com.dndsuite.exceptions.JSONFormatException;
import com.dndsuite.maths.Vector;

class DealsDamageTypeTest {

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
	@DisplayName("Functionality test")
	void test001() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString;

			jsonString = "{\"tags\":[],\"effects\":[],\"tasks\":[]}";
			JSONObject dummyJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(dummyJson);

			jsonString = "{\"tags\":[],\"damage\":[{\"dice\":1,\"size\":6,\"damage_type\":\"cold\"}],\"subevents\":[]}";
			JSONObject eventJson = (JSONObject) parser.parse(jsonString);
			Event event = new Event(eventJson);

			Subevent subevent = new Damage(); // arbitrary subevent type
			subevent.setParentEvent(event);

			// invoke so that the Event determines its base damage dice from json data
			event.invoke(new Vector(), new Vector(), dummy);

			Condition c = new DealsDamageType();
			JSONObject conditionJson;

			jsonString = "{\"condition\":\"deals_damage_type\",\"damage_type\":\"cold\"}";
			conditionJson = (JSONObject) parser.parse(jsonString);
			assertTrue(c.parse(conditionJson, null, subevent));

			jsonString = "{\"condition\":\"deals_damage_type\",\"damage_type\":\"fire\"}";
			conditionJson = (JSONObject) parser.parse(jsonString);
			assertFalse(c.parse(conditionJson, null, subevent));

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (ConditionMismatchException ex) {
			ex.printStackTrace();
			fail("Condition mismatch");
		} catch (JSONFormatException ex) {
			ex.printStackTrace();
			fail("JSON format exception");
		}
	}

}
