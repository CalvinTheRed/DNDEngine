package com.dndsuite.maths.dice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.dndsuite.maths.dice.Die;

public class DieTest {

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
		Die d = new Die(1);
		assertEquals(1, d.getSize());
	}

	@Test
	@DisplayName("Die rolling")
	void test2() {
		Die d = new Die(1);
		d.roll();
		assertEquals(1, d.getRoll());
	}

	@Test
	@DisplayName("Die cloning")
	void test3() {
		Die d1 = new Die(1);
		Die d2 = d1.clone();
		assertNotEquals(d1, d2);
		assertEquals(d1.getSize(), d2.getSize());
		assertEquals(d1.getRoll(), d2.getRoll());
		d2.roll();
		assertNotEquals(d1.getRoll(), d2.getRoll());
	}
	
	@Test
	@DisplayName("Die control mode")
	void test4() {
		Die d = new Die(1);
		int [] controlValues = {1,2,3,4,5,6,7,8,9,10};
		Die.enableDiceControl(controlValues);
		for (int i = 0; i < controlValues.length; i++) {
			d.roll();
			assertEquals(controlValues[i], d.getRoll());
		}
		d.roll();
		assertEquals(1, d.getRoll());
	}

}
