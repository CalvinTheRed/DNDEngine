package com.dndsuite.core.event_groups;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.dndsuite.core.GameObject;
import com.dndsuite.core.Item;
import com.dndsuite.core.Task;
import com.dndsuite.core.UUIDTable;
import com.dndsuite.core.VirtualBoard;
import com.dndsuite.exceptions.CannotUnequipItemException;
import com.dndsuite.exceptions.UUIDNotAssignedException;

class ItemAttackGroupTest {

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
	@DisplayName("Unarmed attacks")
	void test001() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString;

			// Construct Task
			jsonString = "{\"tags\":[],\"item_attack_groups\":[{\"hand\":\"mainhand\",\"hit\":[],\"miss\":[]},{\"hand\":\"mainhand\",\"hit\":[],\"miss\":[]}]}";
			JSONObject taskJson = (JSONObject) parser.parse(jsonString);
			Task task = new Task(taskJson);
			long taskUUID = task.getUUID();

			// construct GameObject dummy
			jsonString = "{\"effects\":[],\"tags\":[],\"inventory\":{\"mainhand\":-1,\"offhand\":-1,\"items\":[]},"
					+ "\"tasks\":[" + taskUUID + "]}";
			JSONObject dummyJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(dummyJson);

			// invoke Task to generate EventGroups
			assertEquals(0, dummy.getQueuedEventGroups().size()); // 0 EventGroups present by default
			dummy.invokeTask(taskUUID);
			assertEquals(2, dummy.getQueuedEventGroups().size()); // 2 EventGroups listed in jsonString

			// verify contents of EventGroups)
			JSONObject eventJson;
			JSONArray subevents;
			JSONObject subevent;
			assertEquals(1, dummy.getQueuedEventGroups().get(0).getEvents().size());
			assertEquals("Unarmed Strike", dummy.getQueuedEventGroups().get(0).getEvents().get(0).toString());
			eventJson = dummy.getQueuedEventGroups().get(0).getEvents().get(0).getJSONData();
			subevents = (JSONArray) eventJson.get("subevents");
			subevent = (JSONObject) subevents.get(0);
			assertEquals("str", subevent.get("attack_ability"));

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (UUIDNotAssignedException ex) {
			ex.printStackTrace();
			fail("UUID not assigned");
		}
	}

	@Test
	@DisplayName("Improvised item attacks")
	void test002() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString;

			// Construct Task
			jsonString = "{\"tags\":[],\"item_attack_groups\":[{\"hand\":\"mainhand\",\"hit\":[],\"miss\":[]},{\"hand\":\"mainhand\",\"hit\":[],\"miss\":[]}]}";
			JSONObject taskJson = (JSONObject) parser.parse(jsonString);
			Task task = new Task(taskJson);
			long taskUUID = task.getUUID();

			// construct Item
			jsonString = "{\"tags\":[],\"equipped_effects\":[]}";
			JSONObject itemJson = (JSONObject) parser.parse(jsonString);
			Item item = new Item(itemJson);
			long itemUUID = item.getUUID();

			// construct GameObject dummy
			jsonString = "{\"effects\":[],\"tags\":[],\"inventory\":{\"mainhand\":-1,\"offhand\":-1,\"items\":[]},"
					+ "\"tasks\":[" + taskUUID + "]}";
			JSONObject dummyJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(dummyJson);
			dummy.equipMainhand(itemUUID);

			// invoke Task to generate EventGroups
			assertEquals(0, dummy.getQueuedEventGroups().size()); // 0 EventGroups present by default
			dummy.invokeTask(taskUUID);
			assertEquals(2, dummy.getQueuedEventGroups().size()); // 2 EventGroups listed in jsonString

			// verify contents of EventGroups
			JSONObject eventJson;
			JSONArray subevents;
			JSONObject subevent;
			assertEquals(2, dummy.getQueuedEventGroups().get(0).getEvents().size());
			assertEquals("Melee Attack", dummy.getQueuedEventGroups().get(0).getEvents().get(0).toString());
			eventJson = dummy.getQueuedEventGroups().get(0).getEvents().get(0).getJSONData();
			subevents = (JSONArray) eventJson.get("subevents");
			subevent = (JSONObject) subevents.get(0);
			assertEquals("str", subevent.get("attack_ability"));
			assertTrue(dummy.getQueuedEventGroups().get(0).getEvents().get(0).hasTag("improvised"));
			assertEquals("Thrown Attack", dummy.getQueuedEventGroups().get(0).getEvents().get(1).toString());
			assertTrue(dummy.getQueuedEventGroups().get(0).getEvents().get(1).hasTag("improvised"));
			eventJson = dummy.getQueuedEventGroups().get(0).getEvents().get(1).getJSONData();
			subevents = (JSONArray) eventJson.get("subevents");
			subevent = (JSONObject) subevents.get(0);
			assertEquals("str", subevent.get("attack_ability"));

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (UUIDNotAssignedException ex) {
			ex.printStackTrace();
			fail("UUID not assigned");
		} catch (CannotUnequipItemException ex) {
			ex.printStackTrace();
			fail("Could not unequip item");
		}
	}

	@Test
	@DisplayName("Melee item attacks")
	void test003() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString;

			// Construct Task
			jsonString = "{\"tags\":[],\"item_attack_groups\":[{\"hand\":\"mainhand\",\"hit\":[],\"miss\":[]},{\"hand\":\"mainhand\",\"hit\":[],\"miss\":[]}]}";
			JSONObject taskJson = (JSONObject) parser.parse(jsonString);
			Task task = new Task(taskJson);
			long taskUUID = task.getUUID();

			// construct Item
			jsonString = "{\"tags\":[\"melee\"],\"equipped_effects\":[]}";
			JSONObject itemJson = (JSONObject) parser.parse(jsonString);
			Item item = new Item(itemJson);
			long itemUUID = item.getUUID();

			// construct GameObject dummy
			jsonString = "{\"effects\":[],\"tags\":[],\"inventory\":{\"mainhand\":-1,\"offhand\":-1,\"items\":[]},"
					+ "\"tasks\":[" + taskUUID + "]}";
			JSONObject dummyJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(dummyJson);
			dummy.equipMainhand(itemUUID);

			// invoke Task to generate EventGroups
			assertEquals(0, dummy.getQueuedEventGroups().size()); // 0 EventGroups present by default
			dummy.invokeTask(taskUUID);
			assertEquals(2, dummy.getQueuedEventGroups().size()); // 2 EventGroups listed in jsonString

			// verify contents of EventGroups
			JSONObject eventJson;
			JSONArray subevents;
			JSONObject subevent;
			assertEquals(2, dummy.getQueuedEventGroups().get(0).getEvents().size());
			assertEquals("Melee Attack", dummy.getQueuedEventGroups().get(0).getEvents().get(0).toString());
			assertFalse(dummy.getQueuedEventGroups().get(0).getEvents().get(0).hasTag("improvised"));
			eventJson = dummy.getQueuedEventGroups().get(0).getEvents().get(0).getJSONData();
			subevents = (JSONArray) eventJson.get("subevents");
			subevent = (JSONObject) subevents.get(0);
			assertEquals("str", subevent.get("attack_ability"));
			assertEquals("Thrown Attack", dummy.getQueuedEventGroups().get(0).getEvents().get(1).toString());
			assertTrue(dummy.getQueuedEventGroups().get(0).getEvents().get(1).hasTag("improvised"));
			eventJson = dummy.getQueuedEventGroups().get(0).getEvents().get(1).getJSONData();
			subevents = (JSONArray) eventJson.get("subevents");
			subevent = (JSONObject) subevents.get(0);
			assertEquals("str", subevent.get("attack_ability"));

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (UUIDNotAssignedException ex) {
			ex.printStackTrace();
			fail("UUID not assigned");
		} catch (CannotUnequipItemException ex) {
			ex.printStackTrace();
			fail("Could not unequip item");
		}
	}

	@Test
	@DisplayName("Melee, thrown item attacks")
	void test004() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString;

			// Construct Task
			jsonString = "{\"tags\":[],\"item_attack_groups\":[{\"hand\":\"mainhand\",\"hit\":[],\"miss\":[]},{\"hand\":\"mainhand\",\"hit\":[],\"miss\":[]}]}";
			JSONObject taskJson = (JSONObject) parser.parse(jsonString);
			Task task = new Task(taskJson);
			long taskUUID = task.getUUID();

			// construct Item
			jsonString = "{\"tags\":[\"melee\",\"thrown\"],\"equipped_effects\":[]}";
			JSONObject itemJson = (JSONObject) parser.parse(jsonString);
			Item item = new Item(itemJson);
			long itemUUID = item.getUUID();

			// construct GameObject dummy
			jsonString = "{\"effects\":[],\"tags\":[],\"inventory\":{\"mainhand\":-1,\"offhand\":-1,\"items\":[]},"
					+ "\"tasks\":[" + taskUUID + "]}";
			JSONObject dummyJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(dummyJson);
			dummy.equipMainhand(itemUUID);

			// invoke Task to generate EventGroups
			assertEquals(0, dummy.getQueuedEventGroups().size()); // 0 EventGroups present by default
			dummy.invokeTask(taskUUID);
			assertEquals(2, dummy.getQueuedEventGroups().size()); // 2 EventGroups listed in jsonString

			// verify contents of EventGroups
			JSONObject eventJson;
			JSONArray subevents;
			JSONObject subevent;
			assertEquals(2, dummy.getQueuedEventGroups().get(0).getEvents().size());
			assertEquals("Melee Attack", dummy.getQueuedEventGroups().get(0).getEvents().get(0).toString());
			assertFalse(dummy.getQueuedEventGroups().get(0).getEvents().get(0).hasTag("improvised"));
			eventJson = dummy.getQueuedEventGroups().get(0).getEvents().get(0).getJSONData();
			subevents = (JSONArray) eventJson.get("subevents");
			subevent = (JSONObject) subevents.get(0);
			assertEquals("str", subevent.get("attack_ability"));
			assertEquals("Thrown Attack", dummy.getQueuedEventGroups().get(0).getEvents().get(1).toString());
			assertFalse(dummy.getQueuedEventGroups().get(0).getEvents().get(1).hasTag("improvised"));
			eventJson = dummy.getQueuedEventGroups().get(0).getEvents().get(1).getJSONData();
			subevents = (JSONArray) eventJson.get("subevents");
			subevent = (JSONObject) subevents.get(0);
			assertEquals("str", subevent.get("attack_ability"));

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (UUIDNotAssignedException ex) {
			ex.printStackTrace();
			fail("UUID not assigned");
		} catch (CannotUnequipItemException ex) {
			ex.printStackTrace();
			fail("Could not unequip item");
		}
	}

	@Test
	@DisplayName("Melee, finesse item attacks")
	void test005() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString;

			// Construct Task
			jsonString = "{\"tags\":[],\"item_attack_groups\":[{\"hand\":\"mainhand\",\"hit\":[],\"miss\":[]},{\"hand\":\"mainhand\",\"hit\":[],\"miss\":[]}]}";
			JSONObject taskJson = (JSONObject) parser.parse(jsonString);
			Task task = new Task(taskJson);
			long taskUUID = task.getUUID();

			// construct Item
			jsonString = "{\"tags\":[\"melee\",\"finesse\"],\"equipped_effects\":[]}";
			JSONObject itemJson = (JSONObject) parser.parse(jsonString);
			Item item = new Item(itemJson);
			long itemUUID = item.getUUID();

			// construct GameObject dummy
			jsonString = "{\"effects\":[],\"tags\":[],\"inventory\":{\"mainhand\":-1,\"offhand\":-1,\"items\":[]},"
					+ "\"tasks\":[" + taskUUID + "]}";
			JSONObject dummyJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(dummyJson);
			dummy.equipMainhand(itemUUID);

			// invoke Task to generate EventGroups
			assertEquals(0, dummy.getQueuedEventGroups().size()); // 0 EventGroups present by default
			dummy.invokeTask(taskUUID);
			assertEquals(2, dummy.getQueuedEventGroups().size()); // 2 EventGroups listed in jsonString

			// verify contents of EventGroups
			JSONObject eventJson;
			JSONArray subevents;
			JSONObject subevent;
			assertEquals(3, dummy.getQueuedEventGroups().get(0).getEvents().size());
			assertEquals("Melee Attack", dummy.getQueuedEventGroups().get(0).getEvents().get(0).toString());
			assertFalse(dummy.getQueuedEventGroups().get(0).getEvents().get(0).hasTag("improvised"));
			eventJson = dummy.getQueuedEventGroups().get(0).getEvents().get(0).getJSONData();
			subevents = (JSONArray) eventJson.get("subevents");
			subevent = (JSONObject) subevents.get(0);
			assertEquals("str", subevent.get("attack_ability"));
			assertEquals("Melee Attack", dummy.getQueuedEventGroups().get(0).getEvents().get(1).toString());
			assertFalse(dummy.getQueuedEventGroups().get(0).getEvents().get(1).hasTag("improvised"));
			eventJson = dummy.getQueuedEventGroups().get(0).getEvents().get(1).getJSONData();
			subevents = (JSONArray) eventJson.get("subevents");
			subevent = (JSONObject) subevents.get(0);
			assertEquals("dex", subevent.get("attack_ability"));
			assertEquals("Thrown Attack", dummy.getQueuedEventGroups().get(0).getEvents().get(2).toString());
			assertTrue(dummy.getQueuedEventGroups().get(0).getEvents().get(2).hasTag("improvised"));
			eventJson = dummy.getQueuedEventGroups().get(0).getEvents().get(2).getJSONData();
			subevents = (JSONArray) eventJson.get("subevents");
			subevent = (JSONObject) subevents.get(0);
			assertEquals("str", subevent.get("attack_ability"));

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (UUIDNotAssignedException ex) {
			ex.printStackTrace();
			fail("UUID not assigned");
		} catch (CannotUnequipItemException ex) {
			ex.printStackTrace();
			fail("Could not unequip item");
		}
	}

	@Test
	@DisplayName("Melee, thrown, finesse item attacks")
	void test006() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString;

			// Construct Task
			jsonString = "{\"tags\":[],\"item_attack_groups\":[{\"hand\":\"mainhand\",\"hit\":[],\"miss\":[]},{\"hand\":\"mainhand\",\"hit\":[],\"miss\":[]}]}";
			JSONObject taskJson = (JSONObject) parser.parse(jsonString);
			Task task = new Task(taskJson);
			long taskUUID = task.getUUID();

			// construct Item
			jsonString = "{\"tags\":[\"melee\",\"thrown\",\"finesse\"],\"equipped_effects\":[]}";
			JSONObject itemJson = (JSONObject) parser.parse(jsonString);
			Item item = new Item(itemJson);
			long itemUUID = item.getUUID();

			// construct GameObject dummy
			jsonString = "{\"effects\":[],\"tags\":[],\"inventory\":{\"mainhand\":-1,\"offhand\":-1,\"items\":[]},"
					+ "\"tasks\":[" + taskUUID + "]}";
			JSONObject dummyJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(dummyJson);
			dummy.equipMainhand(itemUUID);

			// invoke Task to generate EventGroups
			assertEquals(0, dummy.getQueuedEventGroups().size()); // 0 EventGroups present by default
			dummy.invokeTask(taskUUID);
			assertEquals(2, dummy.getQueuedEventGroups().size()); // 2 EventGroups listed in jsonString

			// verify contents of EventGroups
			JSONObject eventJson;
			JSONArray subevents;
			JSONObject subevent;
			assertEquals(4, dummy.getQueuedEventGroups().get(0).getEvents().size());
			assertEquals("Melee Attack", dummy.getQueuedEventGroups().get(0).getEvents().get(0).toString());
			assertFalse(dummy.getQueuedEventGroups().get(0).getEvents().get(0).hasTag("improvised"));
			eventJson = dummy.getQueuedEventGroups().get(0).getEvents().get(0).getJSONData();
			subevents = (JSONArray) eventJson.get("subevents");
			subevent = (JSONObject) subevents.get(0);
			assertEquals("str", subevent.get("attack_ability"));
			assertEquals("Melee Attack", dummy.getQueuedEventGroups().get(0).getEvents().get(1).toString());
			assertFalse(dummy.getQueuedEventGroups().get(0).getEvents().get(1).hasTag("improvised"));
			eventJson = dummy.getQueuedEventGroups().get(0).getEvents().get(1).getJSONData();
			subevents = (JSONArray) eventJson.get("subevents");
			subevent = (JSONObject) subevents.get(0);
			assertEquals("dex", subevent.get("attack_ability"));
			assertEquals("Thrown Attack", dummy.getQueuedEventGroups().get(0).getEvents().get(2).toString());
			assertFalse(dummy.getQueuedEventGroups().get(0).getEvents().get(2).hasTag("improvised"));
			eventJson = dummy.getQueuedEventGroups().get(0).getEvents().get(2).getJSONData();
			subevents = (JSONArray) eventJson.get("subevents");
			subevent = (JSONObject) subevents.get(0);
			assertEquals("str", subevent.get("attack_ability"));
			assertEquals("Thrown Attack", dummy.getQueuedEventGroups().get(0).getEvents().get(3).toString());
			assertFalse(dummy.getQueuedEventGroups().get(0).getEvents().get(3).hasTag("improvised"));
			eventJson = dummy.getQueuedEventGroups().get(0).getEvents().get(3).getJSONData();
			subevents = (JSONArray) eventJson.get("subevents");
			subevent = (JSONObject) subevents.get(0);
			assertEquals("dex", subevent.get("attack_ability"));

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (UUIDNotAssignedException ex) {
			ex.printStackTrace();
			fail("UUID not assigned");
		} catch (CannotUnequipItemException ex) {
			ex.printStackTrace();
			fail("Could not unequip item");
		}
	}

	@Test
	@DisplayName("ranged item attacks")
	void test007() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString;

			// Construct Task
			jsonString = "{\"tags\":[],\"item_attack_groups\":[{\"hand\":\"mainhand\",\"hit\":[],\"miss\":[]},{\"hand\":\"mainhand\",\"hit\":[],\"miss\":[]}]}";
			JSONObject taskJson = (JSONObject) parser.parse(jsonString);
			Task task = new Task(taskJson);
			long taskUUID = task.getUUID();

			// construct Item
			jsonString = "{\"tags\":[\"ranged\"],\"equipped_effects\":[],\"range\":{\"short\":120.0,\"long\":600.0}}";
			JSONObject itemJson = (JSONObject) parser.parse(jsonString);
			Item item = new Item(itemJson);
			long itemUUID = item.getUUID();

			// construct GameObject dummy
			jsonString = "{\"effects\":[],\"tags\":[],\"inventory\":{\"mainhand\":-1,\"offhand\":-1,\"items\":[]},"
					+ "\"tasks\":[" + taskUUID + "]}";
			JSONObject dummyJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(dummyJson);
			dummy.equipMainhand(itemUUID);

			// invoke Task to generate EventGroups
			assertEquals(0, dummy.getQueuedEventGroups().size()); // 0 EventGroups present by default
			dummy.invokeTask(taskUUID);
			assertEquals(2, dummy.getQueuedEventGroups().size()); // 2 EventGroups listed in jsonString

			// verify contents of EventGroups
			JSONObject eventJson;
			JSONArray subevents;
			JSONObject subevent;
			JSONObject areaOfEffect;
			JSONObject range;
			assertEquals(3, dummy.getQueuedEventGroups().get(0).getEvents().size());
			assertEquals("Melee Attack", dummy.getQueuedEventGroups().get(0).getEvents().get(0).toString());
			assertTrue(dummy.getQueuedEventGroups().get(0).getEvents().get(0).hasTag("improvised"));
			eventJson = dummy.getQueuedEventGroups().get(0).getEvents().get(0).getJSONData();
			subevents = (JSONArray) eventJson.get("subevents");
			subevent = (JSONObject) subevents.get(0);
			assertEquals("str", subevent.get("attack_ability"));
			assertEquals("Thrown Attack", dummy.getQueuedEventGroups().get(0).getEvents().get(1).toString());
			assertTrue(dummy.getQueuedEventGroups().get(0).getEvents().get(1).hasTag("improvised"));
			eventJson = dummy.getQueuedEventGroups().get(0).getEvents().get(1).getJSONData();
			subevents = (JSONArray) eventJson.get("subevents");
			subevent = (JSONObject) subevents.get(0);
			assertEquals("str", subevent.get("attack_ability"));
			areaOfEffect = (JSONObject) eventJson.get("area_of_effect");
			range = (JSONObject) areaOfEffect.get("range");
			assertEquals(20.0, range.get("short"));
			assertEquals(60.0, range.get("long"));
			assertEquals("Ranged Attack", dummy.getQueuedEventGroups().get(0).getEvents().get(2).toString());
			assertFalse(dummy.getQueuedEventGroups().get(0).getEvents().get(2).hasTag("improvised"));
			eventJson = dummy.getQueuedEventGroups().get(0).getEvents().get(2).getJSONData();
			subevents = (JSONArray) eventJson.get("subevents");
			subevent = (JSONObject) subevents.get(0);
			assertEquals("dex", subevent.get("attack_ability"));
			areaOfEffect = (JSONObject) eventJson.get("area_of_effect");
			range = (JSONObject) areaOfEffect.get("range");
			assertEquals(120.0, range.get("short"));
			assertEquals(600.0, range.get("long"));

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (UUIDNotAssignedException ex) {
			ex.printStackTrace();
			fail("UUID not assigned");
		} catch (CannotUnequipItemException ex) {
			ex.printStackTrace();
			fail("Could not unequip item");
		}
	}

	@Test
	@DisplayName("Invoker changes equipped items")
	void test008() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString;

			// Construct Task
			jsonString = "{\"tags\":[],\"item_attack_groups\":[{\"hand\":\"mainhand\",\"hit\":[],\"miss\":[]},{\"hand\":\"mainhand\",\"hit\":[],\"miss\":[]}]}";
			JSONObject taskJson = (JSONObject) parser.parse(jsonString);
			Task task = new Task(taskJson);
			long taskUUID = task.getUUID();

			// construct Item
			jsonString = "{\"tags\":[\"melee\"],\"equipped_effects\":[]}";
			JSONObject itemJson = (JSONObject) parser.parse(jsonString);
			Item item = new Item(itemJson);
			long itemUUID = item.getUUID();

			// construct GameObject dummy
			jsonString = "{\"effects\":[],\"tags\":[],\"inventory\":{\"mainhand\":-1,\"offhand\":-1,\"items\":[]},"
					+ "\"tasks\":[" + taskUUID + "]}";
			JSONObject dummyJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(dummyJson);
			dummy.equipMainhand(itemUUID);

			// invoke Task to generate EventGroups
			assertEquals(0, dummy.getQueuedEventGroups().size()); // 0 EventGroups present by default
			dummy.invokeTask(taskUUID);
			assertEquals(2, dummy.getQueuedEventGroups().size()); // 2 EventGroups listed in jsonString

			// verify contents of EventGroups
			JSONObject eventJson;
			JSONArray subevents;
			JSONObject subevent;
			assertEquals(2, dummy.getQueuedEventGroups().get(0).getEvents().size());
			assertEquals("Melee Attack", dummy.getQueuedEventGroups().get(0).getEvents().get(0).toString());
			assertFalse(dummy.getQueuedEventGroups().get(0).getEvents().get(0).hasTag("improvised"));
			eventJson = dummy.getQueuedEventGroups().get(0).getEvents().get(0).getJSONData();
			subevents = (JSONArray) eventJson.get("subevents");
			subevent = (JSONObject) subevents.get(0);
			assertEquals("str", subevent.get("attack_ability"));
			assertEquals("Thrown Attack", dummy.getQueuedEventGroups().get(0).getEvents().get(1).toString());
			assertTrue(dummy.getQueuedEventGroups().get(0).getEvents().get(1).hasTag("improvised"));
			eventJson = dummy.getQueuedEventGroups().get(0).getEvents().get(1).getJSONData();
			subevents = (JSONArray) eventJson.get("subevents");
			subevent = (JSONObject) subevents.get(0);
			assertEquals("str", subevent.get("attack_ability"));

			// unequip item
			dummy.stowMainhand();
			assertEquals(null, dummy.getMainhand());

			// verify contents of EventGroups
			assertEquals(1, dummy.getQueuedEventGroups().get(0).getEvents().size());
			assertEquals("Unarmed Strike", dummy.getQueuedEventGroups().get(0).getEvents().get(0).toString());
			eventJson = dummy.getQueuedEventGroups().get(0).getEvents().get(0).getJSONData();
			subevents = (JSONArray) eventJson.get("subevents");
			subevent = (JSONObject) subevents.get(0);
			assertEquals("str", subevent.get("attack_ability"));

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (UUIDNotAssignedException ex) {
			ex.printStackTrace();
			fail("UUID not assigned");
		} catch (CannotUnequipItemException ex) {
			ex.printStackTrace();
			fail("Could not unequip item");
		}
	}

}
