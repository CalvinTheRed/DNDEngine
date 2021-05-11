package com.dndsuite.core.gameobjects;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.json.simple.JSONArray;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.dndsuite.core.UUIDTable;
import com.dndsuite.exceptions.UUIDKeyMissingException;
import com.dndsuite.maths.Vector;

class GameObjectTest {
	static GameObject o;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		UUIDTable.clear();
		o = new GameObject("test_gameobject_source", new Vector(), new Vector());
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	@DisplayName("UUIDTable tests")
	void test001() {
		try {
			// UUIDTable contains UUID of o
			assertTrue(UUIDTable.containsKey(o.getUUID()));

			// UUIDTable contains UUID of o's effects
			JSONArray effectUUIDs = (JSONArray) o.getJSONData().get("effects");
			for (int i = 0; i < effectUUIDs.size(); i++) {
				int uuid = (int) effectUUIDs.get(i);
				assertTrue(UUIDTable.containsKey(uuid));
			}

			// UUIDTable contains UUID of o's effects
			JSONArray taskUUIDs = (JSONArray) o.getJSONData().get("tasks");
			for (int i = 0; i < taskUUIDs.size(); i++) {
				int uuid = (int) taskUUIDs.get(i);
				assertTrue(UUIDTable.containsKey(uuid));
			}
		} catch (UUIDKeyMissingException ex) {
			ex.printStackTrace();
			fail("UUID key(s) missing");
		}
	}

}
