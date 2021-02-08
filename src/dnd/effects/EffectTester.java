package dnd.effects;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import gameobjects.entities.Entity;

@ExtendWith(DummyEffectParameterResolver.class)
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
