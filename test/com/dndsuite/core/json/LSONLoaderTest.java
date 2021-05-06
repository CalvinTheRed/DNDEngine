package com.dndsuite.core.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LSONLoaderTest {
	private static JSONObject json;
	private static JSONLoaderWrapper loader;

	@BeforeAll
	@SuppressWarnings("unchecked")
	static void setUpBeforeClass() throws Exception {
		json = new JSONObject();
		json.put("test", "test val");
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		loader = new JSONLoaderWrapper(json);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	@DisplayName("JSON data loading")
	void test001() {
		assertEquals(json.toString(), loader.getJSONData().toString());
	}

}
