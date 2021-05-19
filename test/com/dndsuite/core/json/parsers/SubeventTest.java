package com.dndsuite.core.json.parsers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.dndsuite.core.GameObject;
import com.dndsuite.core.Item;
import com.dndsuite.core.UUIDTable;
import com.dndsuite.core.json.parsers.subevents.invokable.ItemDamage;
import com.dndsuite.core.json.parsers.subevents.uninvokable.DamageDiceCollection;
import com.dndsuite.core.json.parsers.subevents.uninvokable.UnequipItem;
import com.dndsuite.exceptions.CannotUnequipItemException;
import com.dndsuite.exceptions.JSONFormatException;
import com.dndsuite.exceptions.SubeventMismatchException;
import com.dndsuite.maths.dice.DamageDiceGroup;

class SubeventTest {

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
	}

	@Test
	@DisplayName("ItemDamage")
	@SuppressWarnings("unchecked")
	void test00A() {
		ItemDamage s = new ItemDamage();
		JSONObject iJson;
		JSONObject oJson;
		JSONObject sJson;
		GameObject o;
		Item i;

		// NOTE: this test does not incorporate mainhand ability modifier damage

		long uuid = 1234L;

		iJson = new JSONObject();
		JSONArray damageList = new JSONArray();
		JSONObject damageElement = new JSONObject();
		damageElement.put("dice", 1L);
		damageElement.put("size", 8L);
		damageElement.put("damage_type", "cold");
		damageList.add(damageElement);
		iJson.put("damage", damageList);
		iJson.put("equipped_effects", new JSONArray());
		JSONArray tags = new JSONArray();
		tags.add("versatile");
		iJson.put("tags", tags);
		iJson.put("uuid", uuid);

		i = new Item(iJson);
		UUIDTable.addToTable(i);

		oJson = new JSONObject();
		oJson.put("effects", new JSONArray());
		JSONObject inventory = new JSONObject();
		inventory.put("mainhand", -1L);
		inventory.put("offhand", -1L);
		inventory.put("items", new JSONArray());
		oJson.put("inventory", inventory);
		JSONObject health = new JSONObject();
		health.put("max", 10L);
		health.put("base", 10L);
		health.put("current", 10L);
		health.put("tmp", 0L);
		oJson.put("health", health);

		sJson = new JSONObject();
		sJson.put("subevent", "item_damage");

		o = new GameObject(oJson);
		try {
			ArrayList<DamageDiceGroup> damageDice;
			DamageDiceCollection ddc;
			DamageDiceGroup group;

			// unarmed attack, mainhand
			sJson.put("hand", "mainhand");
			ddc = s.getBaseCollection(sJson, o);
			damageDice = ddc.getDamageDice();
			assertEquals(1, damageDice.size());
			group = damageDice.get(0);
			assertEquals(1, group.getDice().size());
			assertEquals(4, group.getDice().get(0).getSize());
			assertEquals("bludgeoning", group.getDamageType());

			// unarmed attack, offhand
			sJson.put("hand", "offhand");
			ddc = s.getBaseCollection(sJson, o);
			damageDice = ddc.getDamageDice();
			assertEquals(1, damageDice.size());
			group = damageDice.get(0);
			assertEquals(1, group.getDice().size());
			assertEquals(4, group.getDice().get(0).getSize());
			assertEquals("bludgeoning", group.getDamageType());

			// one-handed attack, mainhand
			o.equipMainhand(uuid);
			sJson.put("hand", "mainhand");
			ddc = s.getBaseCollection(sJson, o);
			damageDice = ddc.getDamageDice();
			assertEquals(1, damageDice.size());
			group = damageDice.get(0);
			assertEquals(1, group.getDice().size());
			assertEquals(8, group.getDice().get(0).getSize());
			assertEquals("cold", group.getDamageType());
			o.stowMainhand();

			// one-handed attack, offhand
			o.equipOffhand(uuid);
			sJson.put("hand", "offhand");
			ddc = s.getBaseCollection(sJson, o);
			damageDice = ddc.getDamageDice();
			assertEquals(1, damageDice.size());
			group = damageDice.get(0);
			assertEquals(1, group.getDice().size());
			assertEquals(8, group.getDice().get(0).getSize());
			assertEquals("cold", group.getDamageType());
			o.stowOffhand();

			// versatile attack, mainhand
			o.equipMainhand(uuid);
			o.toggleVersatile();
			sJson.put("hand", "mainhand");
			ddc = s.getBaseCollection(sJson, o);
			damageDice = ddc.getDamageDice();
			assertEquals(1, damageDice.size());
			group = damageDice.get(0);
			assertEquals(1, group.getDice().size());
			assertEquals(10, group.getDice().get(0).getSize());
			assertEquals("cold", group.getDamageType());
			o.stowMainhand();

			// versatile attack, offhand
			o.equipMainhand(uuid);
			o.toggleVersatile();
			sJson.put("hand", "offhand");
			ddc = s.getBaseCollection(sJson, o);
			damageDice = ddc.getDamageDice();
			assertEquals(1, damageDice.size());
			group = damageDice.get(0);
			assertEquals(1, group.getDice().size());
			assertEquals(10, group.getDice().get(0).getSize()); // doesn't upsize
			assertEquals("cold", group.getDamageType());
			o.stowMainhand();

		} catch (CannotUnequipItemException ex) {
			ex.printStackTrace();
			fail("Could not unequip item");
		} catch (JSONFormatException ex) {
			ex.printStackTrace();
			fail("JSON format error");
		}
	}

	@Test
	@DisplayName("UnequipItem")
	@SuppressWarnings("unchecked")
	void test00C() {
		UnequipItem s;
		JSONObject sJson;
		JSONObject oJson;
		JSONObject iJson;
		GameObject o;
		Item i;

		oJson = new JSONObject();
		oJson.put("effects", new JSONArray());
		o = new GameObject(oJson);

		long uuid = 1234L;

		sJson = new JSONObject();
		sJson.put("subevent", "unequip_item");
		sJson.put("item_uuid", uuid);

		iJson = new JSONObject();
		iJson.put("tags", new JSONArray());
		iJson.put("uuid", uuid);
		iJson.put("name", "Test Item");

		i = new Item(iJson);
		s = new UnequipItem();
		o = new GameObject(oJson);

		UUIDTable.addToTable(i);

		try {
			// no changes
			s.parse(sJson, null, o, o);
			assertTrue(s.getSuccess());

			// with prevention
			s.parse(sJson, null, o, o);
			s.prevent();
			assertFalse(s.getSuccess());

			// with permission
			s.parse(sJson, null, o, o);
			s.permit();
			assertTrue(s.getSuccess());

			// with prevention and permission
			s.parse(sJson, null, o, o);
			s.prevent();
			s.permit();
			assertTrue(s.getSuccess());

		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
			fail("Subevent mismatch");
		} catch (JSONFormatException ex) {
			ex.printStackTrace();
			fail("JSON format exception");
		}
	}

}
