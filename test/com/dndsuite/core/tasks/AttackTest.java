package com.dndsuite.core.tasks;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.LinkedList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.dndsuite.core.gameobjects.Entity;
import com.dndsuite.maths.Vector;

class AttackTest {
	protected Entity dummy;
	private Attack attack;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		dummy = new Entity("testing/entities/dummy.lua", new Vector(0,0,0), new Vector(1,0,0));
		attack = new Attack(dummy);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	@DisplayName("Sanity check")
	void test1() {
		assertEquals(1, attack.getNumAttacks());
	}
	
	@Test
	@DisplayName("Add attacks")
	void test2() {
		attack.addAttack();
		assertEquals(2, attack.getNumAttacks());
	}
	
	@Test
	@DisplayName("Task tags")
	void test3() {
		LinkedList<String> tags = attack.getTags();
		LinkedList<String> desired = new LinkedList<String>();
		desired.add("Attack");
		assertIterableEquals(tags, desired);
	}

}
