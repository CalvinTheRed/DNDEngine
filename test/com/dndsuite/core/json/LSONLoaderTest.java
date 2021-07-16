package com.dndsuite.core.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.dndsuite.core.UUIDTable;
import com.dndsuite.core.VirtualBoard;

class LSONLoaderTest {

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
		UUIDTable.clear();
		VirtualBoard.clear();
	}

	@Test
	@DisplayName("JSON data loading")
	void test001() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString = "{\"test_key\":\"test_value\"}";
			JSONObject json = (JSONObject) parser.parse(jsonString);
			JSONLoaderWrapper loader = new JSONLoaderWrapper(json);
			assertEquals(jsonString, loader.getJSONData().toString());
		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parse exception");
		}
	}

}
