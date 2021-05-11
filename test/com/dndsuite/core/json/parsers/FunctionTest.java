package com.dndsuite.core.json.parsers;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.dndsuite.core.effects.Effect;
import com.dndsuite.core.gameobjects.GameObject;
import com.dndsuite.core.json.parsers.functions.GrantAdvantage;
import com.dndsuite.core.json.parsers.functions.GrantDisadvantage;
import com.dndsuite.core.json.parsers.subevents.TestSubevent;
import com.dndsuite.exceptions.FunctionMismatchException;
import com.dndsuite.maths.Vector;

class FunctionTest {
	private static Subevent s;
	private static GameObject source;
	private static GameObject target;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		s = new TestSubevent();
		source = new GameObject("test_gameobject_source", new Vector(), new Vector());
		target = new GameObject("test_gameobject_target", new Vector(), new Vector());
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	@DisplayName("GrantAdvantage")
	@SuppressWarnings("unchecked")
	void test001() {
		GrantAdvantage func = new GrantAdvantage();
		try {
			JSONObject json = new JSONObject();
			json.put("function", "grant_advantage");
			func.parse(json, new Effect("test_effect", source, target), s);
			assertTrue(s.hasTag("advantage"));
		} catch (FunctionMismatchException ex) {
			ex.printStackTrace();
			fail("Function mismatch");
		}
	}

	@Test
	@DisplayName("GrantDisadvantage")
	@SuppressWarnings("unchecked")
	void test002() {
		GrantDisadvantage func = new GrantDisadvantage();
		try {
			JSONObject json = new JSONObject();
			json.put("function", "grant_disadvantage");
			func.parse(json, new Effect("test_effect", source, target), s);
			assertTrue(s.hasTag("disadvantage"));
		} catch (FunctionMismatchException ex) {
			ex.printStackTrace();
			fail("Function mismatch");
		}
	}

}
