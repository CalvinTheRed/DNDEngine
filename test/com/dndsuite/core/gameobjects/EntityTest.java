package com.dndsuite.core.gameobjects;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.dndsuite.maths.Vector;

class EntityTest {

	private Entity dummy;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		Vector here = new Vector();
		Vector heading = new Vector();
		dummy = new Entity("testing/entities/dummy.lua", here, heading);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	@DisplayName("Scriptless Dummy")
	void test() {
		Vector here = new Vector();
		Vector heading = new Vector();
		dummy = new Entity(null, here, heading);
	}

	@Test
	@DisplayName("Scripted Dummy")
	void test2() {
		assertEquals(dummy.getClass(),Entity.class);
	}

	@Test
	@DisplayName("Net Ability Score Modifiers")
	void test3() {
		dummy.setBaseAbilityScore(Entity.STR, 13);
		dummy.setBaseAbilityScore(Entity.DEX, 12);
		dummy.setBaseAbilityScore(Entity.CON, 11);
		dummy.setBaseAbilityScore(Entity.INT, 10);
		dummy.setBaseAbilityScore(Entity.WIS, 9);
		dummy.setBaseAbilityScore(Entity.CHA, 8);
		assertEquals(1,dummy.getAbilityModifier(Entity.STR));
		assertEquals(1,dummy.getAbilityModifier(Entity.DEX));
		assertEquals(0,dummy.getAbilityModifier(Entity.CON));
		assertEquals(0,dummy.getAbilityModifier(Entity.INT));
		assertEquals(-1,dummy.getAbilityModifier(Entity.WIS));
		assertEquals(-1,dummy.getAbilityModifier(Entity.CHA));
	}

	@Test
	@DisplayName("Level and Experience")
	void test4() {
		dummy.setLevel(27);
		dummy.setExperience(4);
		assertEquals(27,dummy.getLevel());
		assertEquals(4,dummy.getExperience());
		dummy.setLevel(28);
		dummy.setExperience(5);
		assertEquals(28,dummy.getLevel());
		assertEquals(5,dummy.getExperience());
	}

}
