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
import com.dndsuite.maths.Vector;
import com.dndsuite.maths.dice.DamageDiceGroup;
import com.dndsuite.maths.dice.Die;

class DamageDiceCollectionTest {

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
	@DisplayName("Add damage dice to contained damage type")
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
			jsonString = "{\"subevent\":\"damage_dice_collection\"}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			DamageDiceCollection damageDiceCollection = parentEvent.getBaseDiceCollection();

			long extraDamageValue = 1L;
			Die.enableDiceControl(new long[] { extraDamageValue });
			DamageDiceGroup newDamageDiceGroup = new DamageDiceGroup(1, 4, "cold");
			damageDiceCollection.addDamageDiceGroup(newDamageDiceGroup);

			// verify addition of damage dice
			assertEquals(1, damageDiceCollection.getDamageDice().size());
			assertEquals(extraDamageValue, damageDiceCollection.getDamageDice().get(0).getDice().get(1).getRoll());

			// verify damage dealt
			damageDiceCollection.parse(subeventJson, parentEvent, dummy, dummy);
			assertEquals(10L - damageValue - extraDamageValue, dummy.getHealth().get("current"));

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		}
	}

	@Test
	@DisplayName("Add damage dice to novel damage type")
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
			jsonString = "{\"subevent\":\"damage_dice_collection\"}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			DamageDiceCollection damageDiceCollection = parentEvent.getBaseDiceCollection();

			long extraDamageValue = 1L;
			Die.enableDiceControl(new long[] { extraDamageValue });
			DamageDiceGroup newDamageDiceGroup = new DamageDiceGroup(1, 4, "fire");
			damageDiceCollection.addDamageDiceGroup(newDamageDiceGroup);

			// verify addition of damage dice
			assertEquals(2, damageDiceCollection.getDamageDice().size());
			assertEquals(extraDamageValue, damageDiceCollection.getDamageDice().get(1).getDice().get(0).getRoll());

			// verify damage dealt
			damageDiceCollection.parse(subeventJson, parentEvent, dummy, dummy);
			assertEquals(10L - damageValue - extraDamageValue, dummy.getHealth().get("current"));

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		}
	}

}
