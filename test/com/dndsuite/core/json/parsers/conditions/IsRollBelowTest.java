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
import com.dndsuite.core.json.parsers.subevents.invokable.AttackRoll;
import com.dndsuite.exceptions.ConditionMismatchException;
import com.dndsuite.exceptions.JSONFormatException;
import com.dndsuite.exceptions.SubeventMismatchException;
import com.dndsuite.maths.dice.Die;

class IsRollBelowTest {

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

			jsonString = "{\"tags\":[],\"effects\":[],\"tasks\":[],"
					+ "\"ability_scores\":{\"str\":10,\"dex\":10,\"con\":10,\"int\":10,\"wis\":10,\"cha\":10}}";
			JSONObject dummyJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(dummyJson);

			jsonString = "{\"tags\":[],\"damage\":[{\"dice\":1,\"size\":6,\"damage_type\":\"cold\"}],\"subevents\":[]}";
			JSONObject eventJson = (JSONObject) parser.parse(jsonString);
			Event event = new Event(eventJson);

			jsonString = "{\"subevent\":\"attack_roll\",\"attack_ability\":\"str\",\"hit\":[],\"miss\":[]}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			AttackRoll subevent = new AttackRoll(); // some d20-rolling subevent
			subevent.setParentEvent(event);
			long roll = 10L;
			Die.enableDiceControl(new long[] { roll });
			subevent.parse(subeventJson, event, dummy, dummy);

			Condition c = new IsRollBelow();
			JSONObject conditionJson;

			// roll is below
			jsonString = "{\"condition\":\"is_roll_below\",\"value\":" + (roll + 1) + "}";
			conditionJson = (JSONObject) parser.parse(jsonString);
			assertTrue(c.parse(conditionJson, null, subevent));

			// roll is above
			jsonString = "{\"condition\":\"is_roll_below\",\"value\":" + (roll - 1) + "}";
			conditionJson = (JSONObject) parser.parse(jsonString);
			assertFalse(c.parse(conditionJson, null, subevent));

			// roll matches
			jsonString = "{\"condition\":\"is_roll_below\",\"value\":" + roll + "}";
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
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		}
	}

}
