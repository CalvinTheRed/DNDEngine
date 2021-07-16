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

import com.dndsuite.core.event_groups.EventGroup;
import com.dndsuite.exceptions.CannotUnequipItemException;
import com.dndsuite.exceptions.JSONFormatException;
import com.dndsuite.exceptions.UUIDDoesNotExistException;
import com.dndsuite.maths.Vector;
import com.dndsuite.maths.dice.Die;

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
	@DisplayName("Template parser check")
	void test001() {
		GameObject dummy = new GameObject("test_gameobject", new Vector(), new Vector());
		assertTrue(UUIDTable.containsKey(dummy.getUUID()));
		assertTrue(VirtualBoard.contains(dummy));
		assertEquals(100L, dummy.getHealth().get("max"));
		assertEquals(100L, dummy.getHealth().get("base"));
		assertEquals(100L, dummy.getHealth().get("current"));
		assertEquals(0L, dummy.getHealth().get("tmp"));
		assertTrue(dummy.hasTag("test"));
		assertEquals(1, dummy.getTasks().size());
		assertEquals(0, dummy.getEffects().size());
		assertEquals("Longsword", dummy.getMainhand().toString());
		assertEquals(10, dummy.getInventory().keySet().size());
	}

	@Test
	@DisplayName("Invoke Task")
	void test002() {
		GameObject dummy = new GameObject("test_gameobject", new Vector(), new Vector());
		dummy.invokeTask(dummy.getTasks().get(0));

		assertEquals(1, dummy.getQueuedEventGroups().size());
		assertEquals("[Melee Attack,Thrown Attack]", dummy.getQueuedEventGroups().get(0).toString());
	}

	@Test
	@DisplayName("Invoke queued Event")
	void test003() {
		try {
			GameObject dummy = new GameObject("test_gameobject", new Vector(), new Vector());
			dummy.invokeTask(dummy.getTasks().get(0));

			EventGroup selectedGroup = dummy.getQueuedEventGroups().get(0);
			Event preferredEvent = selectedGroup.getEvents().get(0);

			dummy.invokeQueuedEvent(preferredEvent);
			assertEquals(0, dummy.getQueuedEventGroups().size());

			Receptor receptor = ReceptorQueue.dequeue();
			assertEquals(preferredEvent, receptor);
			assertEquals(dummy.getUUID(), receptor.getPauseNotes().get("source"));

			JSONParser parser = new JSONParser();
			String jsonString = "{\"responses\":[{\"start_pos\":[0.0,0.0,0.0],\"point_to\":[0.0,0.0,0.0]}]}";
			JSONObject response = (JSONObject) parser.parse(jsonString);

			long damageRoll = 8L;
			long attackRoll = 15L;
			Die.enableDiceControl(new long[] { damageRoll, attackRoll });
			receptor.resume(response);
			assertEquals(100L - damageRoll, (long) dummy.getHealth().get("current"));

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		} catch (JSONFormatException ex) {
			ex.printStackTrace();
			fail("JSON format exception");
		} catch (UUIDDoesNotExistException ex) {
			ex.printStackTrace();
			fail("UUID does not exist");
		}

	}

	@Test
	@DisplayName("Ability Score Modifiers")
	void test004() {
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
	void test005() {
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
		} catch (CannotUnequipItemException ex) {
			ex.printStackTrace();
			fail("Could not unequip item");
		}
	}

	@Test
	@DisplayName("Equip & stow one-handed item (offhand)")
	void test007() {
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
		} catch (CannotUnequipItemException ex) {
			ex.printStackTrace();
			fail("Could not unequip item");
		}
	}

	@Test
	@DisplayName("Equip & stow two-handed item (mainhand)")
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
		} catch (CannotUnequipItemException ex) {
			ex.printStackTrace();
			fail("Could not unequip item");
		}
	}

	@Test
	@DisplayName("Equip & stow two-handed item (offhand)")
	void test009() {
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
		} catch (CannotUnequipItemException ex) {
			ex.printStackTrace();
			fail("Could not unequip item");
		}
	}

	@Test
	@DisplayName("Manipulating versatile items")
	void test00A() {
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
		} catch (CannotUnequipItemException ex) {
			ex.printStackTrace();
			fail("Could not unequip item");
		}
	}

	@Test
	@DisplayName("Take damage value")
	void test00B() {
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
