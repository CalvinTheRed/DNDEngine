package com.dndsuite.core.events;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.LinkedList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.dndsuite.core.events.DamageCalculation;

class DamageCalculationTest {
	private DamageCalculation damagecalc;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		damagecalc = new DamageCalculation(null);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	@DisplayName("Sanity check")
	void test1() {
		assertEquals(0, damagecalc.getTargets().size());
		assertEquals(0, damagecalc.getEffects().size());
		assertEquals(0, damagecalc.getDamageDice().size());
		assertEquals(null, damagecalc.getParent());
		LinkedList<String> desiredTags = new LinkedList<String>();
		desiredTags.add(DamageCalculation.getEventID());
		assertIterableEquals(desiredTags, damagecalc.getTags());
	}

}
