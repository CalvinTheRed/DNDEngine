package com.dndsuite.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.dndsuite.exceptions.CannotUnequipItemException;
import com.dndsuite.exceptions.UUIDNotAssignedException;

class GameObjectTest {

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
	@DisplayName("Added to UUIDTable and VirtualBoard upon construction")
	void test001() {
		GameObject dummy = new GameObject(new JSONObject());

		try {
			assertTrue(UUIDTable.containsKey(dummy.getUUID()));
			assertTrue(VirtualBoard.contains(dummy));
		} catch (UUIDNotAssignedException ex) {
			ex.printStackTrace();
			fail("UUID key(s) missing");
		}
	}

	@Test
	@DisplayName("Invoke Tasks")
	@SuppressWarnings("unchecked")
	void test002() {
		// TODO: this test case requires json files to be created
	}

	@Test
	@DisplayName("Ability Score Modifiers")
	void test003() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString = "{\"ability_scores\":{\"str\":7,\"dex\":8,\"con\":9,\"int\":10,\"wis\":11,\"cha\":12},\"effects\":[]}";
			JSONObject gameObjectJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(gameObjectJson);

			assertEquals(-2L, dummy.getAbilityModifier("str"));
			assertEquals(-1L, dummy.getAbilityModifier("dex"));
			assertEquals(-1L, dummy.getAbilityModifier("con"));
			assertEquals(0L, dummy.getAbilityModifier("int"));
			assertEquals(0L, dummy.getAbilityModifier("wis"));
			assertEquals(1L, dummy.getAbilityModifier("cha"));

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		}
	}

	@Test
	@DisplayName("Proficiency bonus")
	@SuppressWarnings("unchecked")
	void test004() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString = "{}";
			JSONObject dummyJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(dummyJson);

			dummyJson.put("level", 1L);
			assertEquals(2L, dummy.getProficiencyBonus());

			dummyJson.put("level", 4L);
			assertEquals(2L, dummy.getProficiencyBonus());

			dummyJson.put("level", 5L);
			assertEquals(3L, dummy.getProficiencyBonus());

			dummyJson.put("level", 8L);
			assertEquals(3L, dummy.getProficiencyBonus());

			dummyJson.put("level", 9L);
			assertEquals(4L, dummy.getProficiencyBonus());

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		}
	}

	@Test
	@DisplayName("Equip & stow one-handed item (mainhand)")
	void test005() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString;

			// Create item to be wielded
			jsonString = "{\"tags\":[],\"equipped_effects\":[]}";
			JSONObject itemJson = (JSONObject) parser.parse(jsonString);
			Item item = new Item(itemJson);
			long itemUUID = item.getUUID();

			// Create GameObject to wield the item
			jsonString = "{\"inventory\":{\"mainhand\":-1,\"offhand\":-1,\"items\":[]},\"effects\":[]}";
			JSONObject dummyJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(dummyJson);

			// confirm inventory is empty
			assertEquals(null, dummy.getMainhand());
			assertFalse(dummy.hasItem(itemUUID));

			// equip item
			dummy.equipMainhand(itemUUID);
			assertEquals(item, dummy.getMainhand());
			assertTrue(dummy.hasItem(itemUUID));

			// unequip item
			dummy.stowMainhand();
			assertEquals(null, dummy.getMainhand());
			assertTrue(dummy.hasItem(itemUUID));

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
	@DisplayName("Equip & stow one-handed item (offhand)")
	void test006() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString;

			// Create item to be wielded
			jsonString = "{\"tags\":[],\"equipped_effects\":[]}";
			JSONObject itemJson = (JSONObject) parser.parse(jsonString);
			Item item = new Item(itemJson);
			long itemUUID = item.getUUID();

			// Create GameObject to wield the item
			jsonString = "{\"inventory\":{\"mainhand\":-1,\"offhand\":-1,\"items\":[]},\"effects\":[]}";
			JSONObject dummyJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(dummyJson);

			// confirm inventory is empty
			assertEquals(null, dummy.getOffhand());
			assertFalse(dummy.hasItem(itemUUID));

			// equip item
			dummy.equipOffhand(itemUUID);
			assertEquals(item, dummy.getOffhand());
			assertTrue(dummy.hasItem(itemUUID));

			// unequip item
			dummy.stowOffhand();
			assertEquals(null, dummy.getOffhand());
			assertTrue(dummy.hasItem(itemUUID));

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
	@DisplayName("Equip & stow two-handed item (mainhand)")
	void test007() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString;

			// Create item to be wielded
			jsonString = "{\"tags\":[\"two_handed\"],\"equipped_effects\":[]}";
			JSONObject itemJson = (JSONObject) parser.parse(jsonString);
			Item item = new Item(itemJson);
			long itemUUID = item.getUUID();

			// Create GameObject to wield the item
			jsonString = "{\"inventory\":{\"mainhand\":-1,\"offhand\":-1,\"items\":[]},\"effects\":[]}";
			JSONObject dummyJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(dummyJson);

			// confirm inventory is empty
			assertEquals(null, dummy.getMainhand());
			assertFalse(dummy.hasItem(itemUUID));

			// equip item
			dummy.equipMainhand(itemUUID);
			assertEquals(item, dummy.getMainhand());
			assertEquals(item, dummy.getOffhand());
			assertTrue(dummy.hasItem(itemUUID));

			// unequip item
			dummy.stowMainhand();
			assertEquals(null, dummy.getMainhand());
			assertEquals(null, dummy.getOffhand());
			assertTrue(dummy.hasItem(itemUUID));

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
	@DisplayName("Equip & stow two-handed item (offhand)")
	void test008() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString;

			// Create item to be wielded
			jsonString = "{\"tags\":[\"two_handed\"],\"equipped_effects\":[]}";
			JSONObject itemJson = (JSONObject) parser.parse(jsonString);
			Item item = new Item(itemJson);
			long itemUUID = item.getUUID();

			// Create GameObject to wield the item
			jsonString = "{\"inventory\":{\"mainhand\":-1,\"offhand\":-1,\"items\":[]},\"effects\":[]}";
			JSONObject dummyJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(dummyJson);

			// confirm inventory is empty
			assertEquals(null, dummy.getMainhand());
			assertFalse(dummy.hasItem(itemUUID));

			// equip item
			dummy.equipOffhand(itemUUID);
			assertEquals(item, dummy.getMainhand());
			assertEquals(item, dummy.getOffhand());
			assertTrue(dummy.hasItem(itemUUID));

			// unequip item
			dummy.stowOffhand();
			assertEquals(null, dummy.getMainhand());
			assertEquals(null, dummy.getOffhand());
			assertTrue(dummy.hasItem(itemUUID));

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
	@DisplayName("Manipulating versatile items")
	void test009() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString;

			// Create item to be wielded
			jsonString = "{\"tags\":[\"versatile\"],\"equipped_effects\":[]}";
			JSONObject itemJson = (JSONObject) parser.parse(jsonString);
			Item item = new Item(itemJson);
			long itemUUID = item.getUUID();

			// Create GameObject to wield the item
			jsonString = "{\"inventory\":{\"mainhand\":-1,\"offhand\":-1,\"items\":[]},\"effects\":[]}";
			JSONObject dummyJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(dummyJson);

			// confirm inventory is empty
			assertEquals(null, dummy.getMainhand());
			assertFalse(dummy.hasItem(itemUUID));

			// equip item
			dummy.equipMainhand(itemUUID);
			assertEquals(item, dummy.getMainhand());
			assertEquals(null, dummy.getOffhand());
			assertTrue(dummy.hasItem(itemUUID));

			// switch to two hands
			dummy.toggleVersatile();
			assertEquals(item, dummy.getMainhand());
			assertEquals(item, dummy.getOffhand());

			// switch to one hand
			dummy.toggleVersatile();
			assertEquals(item, dummy.getMainhand());
			assertEquals(null, dummy.getOffhand());

			// stow while holding with two hands (mainhand)
			dummy.toggleVersatile();
			dummy.stowMainhand();
			assertEquals(null, dummy.getMainhand());
			assertEquals(null, dummy.getOffhand());

			// stow while holding with two hands (offhand)
			dummy.equipMainhand(itemUUID);
			dummy.toggleVersatile();
			dummy.stowOffhand();
			assertEquals(null, dummy.getMainhand());
			assertEquals(null, dummy.getOffhand());

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
	@DisplayName("Take damage value")
	void test00A() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString = "{\"health\":{\"max\":20,\"base\":20,\"current\":20,\"tmp\":15}}";
			JSONObject dummyJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(dummyJson);

			// reduce temporary hit points
			dummy.takeDamage(10L);
			assertEquals(5L, (long) dummy.getHealth().get("tmp"));
			assertEquals(20L, (long) dummy.getHealth().get("current"));

			// reduce temporary and actual hit points
			dummy.takeDamage(10L);
			assertEquals(0L, (long) dummy.getHealth().get("tmp"));
			assertEquals(15L, (long) dummy.getHealth().get("current"));

			// reduce actual hit points
			dummy.takeDamage(10L);
			assertEquals(0L, (long) dummy.getHealth().get("tmp"));
			assertEquals(5L, (long) dummy.getHealth().get("current"));

			// reduce actual hit points below 0
			dummy.takeDamage(10L);
			assertEquals(0L, (long) dummy.getHealth().get("tmp"));
			assertEquals(0L, (long) dummy.getHealth().get("current"));

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		}
	}

}
