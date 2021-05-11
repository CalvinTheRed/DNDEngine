package com.dndsuite.core.json.parsers;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.dndsuite.core.UUIDTable;
import com.dndsuite.core.effects.Effect;
import com.dndsuite.core.gameobjects.GameObject;
import com.dndsuite.core.json.parsers.conditions.HasTag;
import com.dndsuite.core.json.parsers.subevents.TestSubevent;
import com.dndsuite.exceptions.ConditionMismatchException;

class ConditionTest {
	private static Condition c;
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
	}

	@AfterEach
	void tearDown() throws Exception {
		c = null;
		source = null;
		target = null;
		UUIDTable.clear();
	}

	@Test
	@DisplayName("HasTag")
	@SuppressWarnings("unchecked")
	void test001() {
		c = new HasTag();
		JSONObject cJson;

		cJson = new JSONObject();
		cJson.put("condition", "has_tag");
		cJson.put("tag", "test");

		Effect e = new Effect(null);
		Subevent s = new TestSubevent();

		try {
			assertFalse(c.parse(cJson, e, s));
		} catch (ConditionMismatchException ex) {
			ex.printStackTrace();
			fail("Condition mismatch");
		}

		s.addTag("test");

		try {
			assertTrue(c.parse(cJson, e, s));
		} catch (ConditionMismatchException ex) {
			ex.printStackTrace();
			fail("Condition mismatch");
		}
	}

}
