package com.dndsuite.core.json.parsers.subevents.uninvokable;

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
import com.dndsuite.core.Item;
import com.dndsuite.core.UUIDTable;
import com.dndsuite.core.VirtualBoard;
import com.dndsuite.exceptions.CannotUnequipItemException;
import com.dndsuite.exceptions.SubeventMismatchException;
import com.dndsuite.exceptions.UUIDNotAssignedException;

class UnequipItemTest {

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
	@DisplayName("Normal fallout")
	void test001() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString;

			// set up parent Event
			jsonString = "{\"tags\":[]}";
			JSONObject eventJson = (JSONObject) parser.parse(jsonString);
			Event parentEvent = new Event(eventJson);

			// set up item
			jsonString = "{\"tags\":[],\"equipped_effects\":[]}";
			JSONObject itemJson = (JSONObject) parser.parse(jsonString);
			Item item = new Item(itemJson);
			long itemUUID = item.getUUID();

			// set up dummy GameObject actor
			jsonString = "{\"inventory\":{\"mainhand\":-1,\"offhand\":-1,\"items\":[]},\"effects\":[]}";
			JSONObject dummyJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(dummyJson);
			dummy.equipMainhand(itemUUID);

			// set up UnequipItem subevent
			jsonString = "{\"subevent\":\"unequip_item\",\"item_uuid\":" + itemUUID + "}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			UnequipItem unequipItem = new UnequipItem();

			// check success
			unequipItem.parse(subeventJson, parentEvent, dummy, dummy);
			assertTrue(unequipItem.getSuccess());

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		} catch (UUIDNotAssignedException ex) {
			ex.printStackTrace();
			fail("UUID not assigned");
		} catch (CannotUnequipItemException ex) {
			ex.printStackTrace();
			fail("Could not unequip item");
		}
	}

	@Test
	@DisplayName("Prevent unequip")
	void test002() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString;

			// set up parent Event
			jsonString = "{\"tags\":[]}";
			JSONObject eventJson = (JSONObject) parser.parse(jsonString);
			Event parentEvent = new Event(eventJson);

			// set up item
			jsonString = "{\"tags\":[],\"equipped_effects\":[]}";
			JSONObject itemJson = (JSONObject) parser.parse(jsonString);
			Item item = new Item(itemJson);
			long itemUUID = item.getUUID();

			// set up dummy GameObject actor
			jsonString = "{\"inventory\":{\"mainhand\":-1,\"offhand\":-1,\"items\":[]},\"effects\":[]}";
			JSONObject dummyJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(dummyJson);
			dummy.equipMainhand(itemUUID);

			// set up UnequipItem subevent
			jsonString = "{\"subevent\":\"unequip_item\",\"item_uuid\":" + itemUUID + "}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			UnequipItem unequipItem = new UnequipItem();

			// prevent & check success
			unequipItem.parse(subeventJson, parentEvent, dummy, dummy);
			unequipItem.prevent();
			assertFalse(unequipItem.getSuccess());

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		} catch (UUIDNotAssignedException ex) {
			ex.printStackTrace();
			fail("UUID not assigned");
		} catch (CannotUnequipItemException ex) {
			ex.printStackTrace();
			fail("Could not unequip item");
		}
	}

	@Test
	@DisplayName("Permit unequip")
	void test003() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString;

			// set up parent Event
			jsonString = "{\"tags\":[]}";
			JSONObject eventJson = (JSONObject) parser.parse(jsonString);
			Event parentEvent = new Event(eventJson);

			// set up item
			jsonString = "{\"tags\":[],\"equipped_effects\":[]}";
			JSONObject itemJson = (JSONObject) parser.parse(jsonString);
			Item item = new Item(itemJson);
			long itemUUID = item.getUUID();

			// set up dummy GameObject actor
			jsonString = "{\"inventory\":{\"mainhand\":-1,\"offhand\":-1,\"items\":[]},\"effects\":[]}";
			JSONObject dummyJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(dummyJson);
			dummy.equipMainhand(itemUUID);

			// set up UnequipItem subevent
			jsonString = "{\"subevent\":\"unequip_item\",\"item_uuid\":" + itemUUID + "}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			UnequipItem unequipItem = new UnequipItem();

			// prevent & check success
			unequipItem.parse(subeventJson, parentEvent, dummy, dummy);
			unequipItem.permit();
			assertTrue(unequipItem.getSuccess());

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		} catch (UUIDNotAssignedException ex) {
			ex.printStackTrace();
			fail("UUID not assigned");
		} catch (CannotUnequipItemException ex) {
			ex.printStackTrace();
			fail("Could not unequip item");
		}
	}

	@Test
	@DisplayName("Prevent & permit unequip")
	void test004() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString;

			// set up parent Event
			jsonString = "{\"tags\":[]}";
			JSONObject eventJson = (JSONObject) parser.parse(jsonString);
			Event parentEvent = new Event(eventJson);

			// set up item
			jsonString = "{\"tags\":[],\"equipped_effects\":[]}";
			JSONObject itemJson = (JSONObject) parser.parse(jsonString);
			Item item = new Item(itemJson);
			long itemUUID = item.getUUID();

			// set up dummy GameObject actor
			jsonString = "{\"inventory\":{\"mainhand\":-1,\"offhand\":-1,\"items\":[]},\"effects\":[]}";
			JSONObject dummyJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(dummyJson);
			dummy.equipMainhand(itemUUID);

			// set up UnequipItem subevent
			jsonString = "{\"subevent\":\"unequip_item\",\"item_uuid\":" + itemUUID + "}";
			JSONObject subeventJson = (JSONObject) parser.parse(jsonString);
			UnequipItem unequipItem = new UnequipItem();

			// prevent & check success
			unequipItem.parse(subeventJson, parentEvent, dummy, dummy);
			unequipItem.prevent();
			unequipItem.permit();
			assertTrue(unequipItem.getSuccess());

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		} catch (UUIDNotAssignedException ex) {
			ex.printStackTrace();
			fail("UUID not assigned");
		} catch (CannotUnequipItemException ex) {
			ex.printStackTrace();
			fail("Could not unequip item");
		}
	}

}
