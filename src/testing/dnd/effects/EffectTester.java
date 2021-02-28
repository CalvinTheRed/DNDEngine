package testing.dnd.effects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import maths.Vector;
import testing.gameobjects.entities.DummyEntity;

class EffectTester {

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
	@DisplayName("Effect contructor works")
	void test1() {
		DummyEntity source = new DummyEntity("Dummy Entity", new Vector(0, 0, 0), new Vector(1, 0, 0));
		DummyEntity target = new DummyEntity("Dummy Entity", new Vector(0, 0, 0), new Vector(1, 0, 0));
		DummyEffect e = new DummyEffect(source, target, "Dummy Effect");
		assertEquals(e.getSource(), source);
		assertEquals(e.getTarget(), target);
		assertFalse(e.isEnded());
	}

	@Test
	@DisplayName("Effects can be ended")
	void test3() {
		DummyEffect e = new DummyEffect(null, null, "Dummy Effect");
		e.end();
		assertTrue(e.isEnded());
	}

}
