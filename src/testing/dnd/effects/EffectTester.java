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
import org.junit.jupiter.api.extension.ExtendWith;

import maths.Vector;
import testing.gameobjects.entities.DummyEntity;

@ExtendWith(DummyEffectParameterResolver.class)
class EffectTester {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		DummyEffectParameterResolver.Source = new DummyEntity("Dummy Entity Source", new Vector(0,0,0), new Vector(1,1,1) );
		DummyEffectParameterResolver.Target = new DummyEntity("Dummy Entity Target", new Vector(1,0,0), new Vector(1,0,0) );
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
	@DisplayName("Attributes are managed")
	void test1(DummyEffect E) {
		assertEquals(E.toString(),"Dummy Effect");
		assertEquals(E.getSource(),DummyEffectParameterResolver.Source);
		assertEquals(E.getTarget(),DummyEffectParameterResolver.Target);
	}
	
	@Test
	@DisplayName("Effects are created as NOT ended")
	void test2(DummyEffect E) {
		assertFalse(E.isEnded());
	}
	
	@Test
	@DisplayName("Effects can be ended")
	void test3(DummyEffect E) {
		E.end();
		assertTrue(E.isEnded());
	}
	
}
