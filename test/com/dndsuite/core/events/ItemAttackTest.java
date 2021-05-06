package com.dndsuite.core.events;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.LinkedList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.dndsuite.core.Item;
import com.dndsuite.core.effects.Effect;
import com.dndsuite.core.gameobjects.Entity;
import com.dndsuite.maths.Vector;

class ItemAttackTest {
	
	// TODO: complete this unit test

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
	@DisplayName("Sanity check")
	void test1() {
		int eventAbility = Entity.STR;
		Item medium = new Item("testing/items/dummy.lua");
		String attackType = ItemAttack.MELEE;
		ItemAttack itemAttack = new ItemAttack(eventAbility, medium, attackType);
		assertEquals(eventAbility, itemAttack.getEventAbility());
		assertEquals(medium, itemAttack.getMedium());
		assertEquals(attackType, itemAttack.getAttackType());
		assertEquals("Item Attack (" + attackType + "," + Entity.getAbilityString(eventAbility) + ")", itemAttack.toString());
		
		LinkedList<String> desired = new LinkedList<String>();
		desired.add(ItemAttack.getEventID());
		assertIterableEquals(desired, itemAttack.getTags());
	}
	
	@Test
	@DisplayName("ItemAttack cloning")
	void test2() {
		int eventAbility = Entity.INT;
		Item medium = new Item("testing/items/dummy.lua");
		String attackType = ItemAttack.MELEE;
		ItemAttack itemAttack = new ItemAttack(eventAbility, medium, attackType);
		
		// Populate data fields with seed data to be cloned
		try {
			itemAttack.applyEffect(new Effect("testing/effects/dummy.lua", null, null));
		} catch (Exception ex) {
			fail("Effect failed to apply to default ItemAttack object");
		}
		itemAttack.getTargets().add(new Entity("testing/entities/dummy.lua", new Vector(), new Vector()));
		
		// Clone the ItemAttack
		ItemAttack clone = itemAttack.clone();
		
		// Verify all data fields were successfully cloned
		assertEquals(itemAttack.getAttackType(), clone.getAttackType());
		assertIterableEquals(itemAttack.getEffects(), clone.getEffects());
		assertEquals(itemAttack.getEventAbility(), clone.getEventAbility());
		assertEquals(itemAttack.getGlobals(), clone.getGlobals());
		assertEquals(itemAttack.getMedium(), clone.getMedium());
		assertEquals(itemAttack.getRadius(), clone.getRadius());
		assertArrayEquals(itemAttack.getRange(), clone.getRange());
		assertIterableEquals(itemAttack.getTags(), clone.getTags());
		assertIterableEquals(itemAttack.getTargets(), clone.getTargets());
		
		// Verify that changing one object does not influence the other
		
	}

}
