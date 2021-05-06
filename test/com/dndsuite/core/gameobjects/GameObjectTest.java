package com.dndsuite.core.gameobjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.dndsuite.maths.Vector;

class GameObjectTest {
	static Vector pos;
	static Vector rot;
	static JSONObject json;
	static GameObject o;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	@SuppressWarnings("unchecked")
	void setUp() throws Exception {
		pos = new Vector(1, 2, 3);
		rot = new Vector(4, 5, 6);

		json = new JSONObject();
		json.put("name", "Zombie");
		json.put("size", "medium");
		json.put("alignment", "neutral evil");
		json.put("creature_type", "undead");

		JSONObject health = new JSONObject();
		health.put("base", 22);
		health.put("current", 22);
		health.put("max", 22);
		health.put("tmp", 0);
		json.put("health", health);

//		JSONArray tags = new JSONArray();
//		tags.add("undead");
//		json.put("tags", tags);

		o = new GameObject(json, pos, rot);

		System.out.println(json);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	@DisplayName("Sanity check")
	void test001() {
		assertTrue(o.getPos().equals(pos));
		assertTrue(o.getRot().equals(rot));
		assertEquals(json.toString(), o.getJSONData().toString());
	}

}
