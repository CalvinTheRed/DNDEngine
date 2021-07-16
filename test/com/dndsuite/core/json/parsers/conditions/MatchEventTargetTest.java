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

import com.dndsuite.core.Effect;
import com.dndsuite.core.Event;
import com.dndsuite.core.UUIDTable;
import com.dndsuite.core.VirtualBoard;
import com.dndsuite.core.json.parsers.Condition;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.core.json.parsers.subevents.invokable.Damage;
import com.dndsuite.exceptions.ConditionMismatchException;
import com.dndsuite.exceptions.JSONFormatException;

class MatchEventTargetTest {

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

			long effectUUID = 1L;
			long sourceUUID = 2L;
			long targetUUID = 3L;

			// manually "load" the Effect on the source
			jsonString = "{\"name\":\"Test Effect\",\"tags\":[],\"filters\":[{\"conditions\":["
					+ "{\"condition\":\"match_event_target\",\"event_target\":\"effect_source\"}],\"functions\":[]}],"
					+ "\"source\":" + sourceUUID + ",\"target\":" + targetUUID + ",\"uuid\":" + effectUUID + "}";
			JSONObject effectJson = (JSONObject) parser.parse(jsonString);
			Effect effect = new Effect(effectJson);

			// construct the parent Event
			jsonString = "{\"name\":\"Test Event\",\"tags\":[],\"source\":" + sourceUUID
					+ ",\"damage\":[{\"dice\":1,\"size\":8,\"damage_type\":\"cold\"}]}";
			JSONObject eventJson = (JSONObject) parser.parse(jsonString);
			Event event = new Event(eventJson);

			// construct an arbitrary Subevent
			Subevent subevent = new Damage();
			subevent.setParentEvent(event);
			subevent.setTarget(targetUUID);

			// create MatchEventSource Condition
			Condition c = new MatchEventTarget();

			// create Condition json object
			jsonString = "{\"condition\":\"match_event_target\",\"event_target\":\"effect_source\"}";
			JSONObject conditionJson;

			// check if matching to effect_source returns false as intended
			jsonString = "{\"condition\":\"match_event_target\",\"event_target\":\"effect_source\"}";
			conditionJson = (JSONObject) parser.parse(jsonString);
			assertFalse(c.parse(conditionJson, effect, subevent));

			// check if matching to effect_target returns true as intended
			jsonString = "{\"condition\":\"match_event_target\",\"event_target\":\"effect_target\"}";
			conditionJson = (JSONObject) parser.parse(jsonString);
			assertTrue(c.parse(conditionJson, effect, subevent));

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
