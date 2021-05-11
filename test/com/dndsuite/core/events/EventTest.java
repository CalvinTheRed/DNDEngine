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
	static GameObject o;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		o = new GameObject("test_gameobject", new Vector(), new Vector());
		System.out.println(o);
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
	@DisplayName("Events may be invoked arbitrarily")
	void test001() {
		Event e = new Event("test_event");
		e.invoke(new Vector(), o);
	}

}
