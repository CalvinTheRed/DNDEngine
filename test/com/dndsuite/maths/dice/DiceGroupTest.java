package com.dndsuite.maths.dice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.dndsuite.maths.dice.DiceGroup;
import com.dndsuite.maths.dice.Die;

public class DiceGroupTest {

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
		int numDice = 4;
		int dieSize = 1;
		DiceGroup group = new DiceGroup(numDice, dieSize);
		assertEquals(group.getBonus(), 0);
		LinkedList<Die> list = group.getDice();
		assertEquals(list.size(), numDice);
		for (Die d : list) {
			assertEquals(d.getSize(), dieSize);
		}
	}

	@Test
	@DisplayName("Adding dice")
	void test2() {
		DiceGroup group = new DiceGroup(0, 1);
		Die d = new Die(1);
		group.addDie(d);
		assertTrue(group.getDice().contains(d));
	}

	@Test
	@DisplayName("Adding bonus")
	void test3() {
		int bonus = 4;
		DiceGroup group = new DiceGroup(0, 1);
		group.addBonus(bonus);
		assertEquals(group.getBonus(), bonus);
	}

	@Test
	@DisplayName("Rolling dice")
	void test4() {
		int numDice = 4;
		int dieSize = 1;
		int bonus = 10;
		DiceGroup group = new DiceGroup(numDice, dieSize);
		group.addBonus(bonus);
		group.roll();
		assertEquals(group.getSum(), numDice + bonus);
	}

}
