package com.dndsuite.core.events;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.dndsuite.core.gameobjects.GameObject;
import com.dndsuite.maths.Vector;

class EventTest {
	static GameObject source;
	static GameObject target;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		source = new GameObject("test_gameobject_source", new Vector(5, 5, 5), new Vector(5, 5, 5));
		target = new GameObject("test_gameobject_target", new Vector(1, 1, 1), new Vector(1, 1, 1));
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
	@DisplayName("Proof of functionality event invocation")
	void test001() {
		Event e = new Event("test_event");
		e.invoke(target.getPos(), source);
	}

}
