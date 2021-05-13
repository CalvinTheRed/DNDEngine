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

import com.dndsuite.core.json.parsers.functions.GrantAdvantage;
import com.dndsuite.core.json.parsers.functions.GrantDisadvantage;
import com.dndsuite.core.json.parsers.subevents.AttackRoll;
import com.dndsuite.exceptions.FunctionMismatchException;

class FunctionTest {

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
	@DisplayName("GrantAdvantage")
	@SuppressWarnings("unchecked")
	void test001() {
		Function f;
		Subevent s;

		JSONObject fJson = new JSONObject();
		fJson.put("function", "grant_advantage");

		JSONObject oJson = new JSONObject();

		f = new GrantAdvantage();
		s = new AttackRoll();

		try {
			f.parse(fJson, null, s);
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
		Function f;
		Subevent s;

		JSONObject fJson = new JSONObject();
		fJson.put("function", "grant_disadvantage");

		JSONObject oJson = new JSONObject();

		f = new GrantDisadvantage();
		s = new AttackRoll();

		try {
			f.parse(fJson, null, s);
			assertTrue(s.hasTag("disadvantage"));
		} catch (FunctionMismatchException ex) {
			ex.printStackTrace();
			fail("Function mismatch");
		}
	}

}
