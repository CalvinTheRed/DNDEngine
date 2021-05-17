package com.dndsuite.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
		VirtualBoard.clearGameObjects();
	}

	@Test
	@DisplayName("UUIDTable loading")
	void test001() {
		GameObject o = new GameObject(new JSONObject());

		try {
			assertTrue(UUIDTable.containsKey(o.getUUID()));
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
	@SuppressWarnings("unchecked")
	void test003() {
		GameObject o;
		JSONObject oJson;

		oJson = new JSONObject();
		JSONObject abilityScores = new JSONObject();
		abilityScores.put("str", 7L);
		abilityScores.put("dex", 8L);
		abilityScores.put("con", 9L);
		abilityScores.put("int", 10L);
		abilityScores.put("wis", 11L);
		abilityScores.put("cha", 12L);
		oJson.put("ability_scores", abilityScores);
		oJson.put("effects", new JSONArray());

		o = new GameObject(oJson);

		assertEquals(-2L, o.getAbilityModifier("str"));
		assertEquals(-1L, o.getAbilityModifier("dex"));
		assertEquals(-1L, o.getAbilityModifier("con"));
		assertEquals(0L, o.getAbilityModifier("int"));
		assertEquals(0L, o.getAbilityModifier("wis"));
		assertEquals(1L, o.getAbilityModifier("cha"));
	}

	@Test
	@DisplayName("Proficiency bonus")
	@SuppressWarnings("unchecked")
	void test004() {
		GameObject o;
		JSONObject oJson;

		oJson = new JSONObject();
		oJson.put("level", 1L);
		o = new GameObject(oJson);
		assertEquals(2, o.getProficiencyBonus());

		oJson = new JSONObject();
		oJson.put("level", 4L);
		o = new GameObject(oJson);
		assertEquals(2, o.getProficiencyBonus());

		oJson = new JSONObject();
		oJson.put("level", 5L);
		o = new GameObject(oJson);
		assertEquals(3, o.getProficiencyBonus());

		oJson = new JSONObject();
		oJson.put("level", 8L);
		o = new GameObject(oJson);
		assertEquals(3, o.getProficiencyBonus());

		oJson = new JSONObject();
		oJson.put("level", 9L);
		o = new GameObject(oJson);
		assertEquals(4, o.getProficiencyBonus());

	}

	@Test
	@DisplayName("Item equipping & unequipping")
	@SuppressWarnings("unchecked")
	void test005() {
		GameObject o;
		JSONObject oJson;
		JSONObject iJson;
		Item i;

		oJson = new JSONObject();
		JSONObject inventory = new JSONObject();
		inventory.put("mainhand", -1L);
		inventory.put("offhand", -1L);
		JSONArray items = new JSONArray();
		inventory.put("items", items);
		oJson.put("inventory", inventory);
		oJson.put("effects", new JSONArray());

		long uuid = 1234L;

		iJson = new JSONObject();
		JSONArray tags = new JSONArray();
		tags.add("versatile");
		iJson.put("tags", tags);
		iJson.put("uuid", uuid);
		iJson.put("name", "Test Item");

		i = new Item(iJson);
		o = new GameObject(oJson);

		UUIDTable.addToTable(i);

		try {
			// mainhand null -> single-handed
			o.equipMainhand(uuid);
			assertTrue(o.hasItem(uuid));
			assertEquals(i, o.getMainhand());
			assertEquals(null, o.getOffhand());

			// mainhand single-handed -> null
			o.stowMainhand();
			assertTrue(o.hasItem(uuid));
			assertEquals(null, o.getMainhand());
			assertEquals(null, o.getOffhand());

			// clear inventory manually
			items.clear();
			assertFalse(o.hasItem(uuid));

			// offhand null -> single-handed
			o.equipOffhand(uuid);
			assertTrue(o.hasItem(uuid));
			assertEquals(null, o.getMainhand());
			assertEquals(i, o.getOffhand());

			// offhand single-handed -> null
			o.stowOffhand();
			assertTrue(o.hasItem(uuid));
			assertEquals(null, o.getMainhand());
			assertEquals(null, o.getOffhand());

			// clear inventory manually
			items.clear();
			assertFalse(o.hasItem(uuid));

			// mainmhand null -> versatile toggle -> null
			o.equipMainhand(uuid);
			assertTrue(o.hasItem(uuid));
			assertEquals(i, o.getMainhand());
			assertEquals(null, o.getOffhand());
			o.toggleVersatile();
			assertEquals(i, o.getOffhand());
			o.toggleVersatile();
			assertEquals(null, o.getOffhand());
			o.toggleVersatile();
			o.stowMainhand();
			assertEquals(null, o.getMainhand());
			assertEquals(null, o.getOffhand());
			o.equipMainhand(uuid);
			o.toggleVersatile();
			assertEquals(i, o.getMainhand());
			assertEquals(i, o.getOffhand());
			o.stowOffhand();
			assertEquals(null, o.getMainhand());
			assertEquals(null, o.getOffhand());

			// offhand versatile (does nothing)
			o.equipOffhand(uuid);
			assertTrue(o.hasItem(uuid));
			assertEquals(null, o.getMainhand());
			assertEquals(i, o.getOffhand());
			o.toggleVersatile();
			assertEquals(null, o.getMainhand());
			assertEquals(i, o.getOffhand());
			o.stowOffhand();
			assertEquals(null, o.getOffhand());

			// clear inventory manually
			items.clear();
			assertFalse(o.hasItem(uuid));
			tags.add("two_handed");

			// mainhand null -> two_handed -> null
			o.equipMainhand(uuid);
			assertEquals(i, o.getMainhand());
			assertEquals(i, o.getOffhand());
			o.stowMainhand();
			assertEquals(null, o.getMainhand());
			assertEquals(null, o.getOffhand());

			// clear inventory manually
			items.clear();
			assertFalse(o.hasItem(uuid));

			// mainhand null -> two_handed -> null
			o.equipOffhand(uuid);
			assertEquals(i, o.getMainhand());
			assertEquals(i, o.getOffhand());
			o.stowOffhand();
			assertEquals(null, o.getMainhand());
			assertEquals(null, o.getOffhand());

		} catch (CannotUnequipItemException ex) {
			ex.printStackTrace();
			fail("Cannot unequip item");
		}
	}

}
