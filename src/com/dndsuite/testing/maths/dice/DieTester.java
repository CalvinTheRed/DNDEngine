package com.dndsuite.testing.maths.dice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.dndsuite.maths.dice.Die;

public class DieTester {

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
	@DisplayName("Die constructor and size getter work")
	void test1() {
		int size = 1;
		Die d = new Die(size);
		assertEquals(d.getSize(), size);
	}

	@Test
	@DisplayName("Die rolling works")
	void test2() {
		Die d = new Die(1);
		d.roll();
		assertEquals(d.getRoll(), 1);
	}

	@Test
	@DisplayName("Die cloning works")
	void test3() {
		int size = 1;
		Die d1 = new Die(size);
		Die d2 = d1.clone();
		assertNotEquals(d1, d2);
		assertEquals(d1.getSize(), d2.getSize());
		assertEquals(d1.getRoll(), d2.getRoll());
		d2.roll();
		assertNotEquals(d1.getRoll(), d2.getRoll());
	}

}
