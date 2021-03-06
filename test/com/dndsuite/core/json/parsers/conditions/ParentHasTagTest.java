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
import com.dndsuite.core.UUIDTable;
import com.dndsuite.core.VirtualBoard;
import com.dndsuite.core.json.parsers.Condition;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.core.json.parsers.subevents.invokable.Damage;
import com.dndsuite.exceptions.ConditionMismatchException;
import com.dndsuite.exceptions.JSONFormatException;

class ParentHasTagTest {

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
	@DisplayName("Functionality test")
	void test001() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString;

			jsonString = "{\"tags\":[],\"subevents\":[]}";
			JSONObject eventJson = (JSONObject) parser.parse(jsonString);
			Event event = new Event(eventJson);

			Subevent subevent = new Damage(); // arbitrary subevent type
			subevent.setParentEvent(event);

			Condition c = new ParentEventHasTag();
			JSONObject conditionJson;

			jsonString = "{\"condition\":\"parent_event_has_tag\",\"tag\":\"test_tag\"}";
			conditionJson = (JSONObject) parser.parse(jsonString);
			assertFalse(c.parse(conditionJson, null, subevent));
			event.addTag("test_tag");
			assertTrue(c.parse(conditionJson, null, subevent));

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
