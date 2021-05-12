package com.dndsuite.core.events;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
	@DisplayName("Damage-dealing Events")
	@SuppressWarnings("unchecked")
	void test002() {
		JSONObject eJson;
		Event e;

		eJson = new JSONObject();
		JSONArray damageList = new JSONArray();
		JSONObject damageElement;
		damageElement = new JSONObject();
		damageElement.put("dice", 1L);
		damageElement.put("size", 1L);
		damageElement.put("damage_type", "slashing");
		damageList.add(damageElement);
		damageElement = new JSONObject();
		damageElement.put("dice", 1L);
		damageElement.put("size", 1L);
		damageElement.put("bonus", 1L);
		damageElement.put("damage_type", "force");
		damageList.add(damageElement);
		eJson.put("damage", damageList);
		JSONObject areaOfEffect = new JSONObject();
		areaOfEffect.put("shape", "single_target");
		areaOfEffect.put("range", "self");
		eJson.put("area_of_effect", areaOfEffect);
		JSONArray subevents = new JSONArray();
		JSONObject subevent;
		subevent = new JSONObject();
		subevent.put("subevent", "damage");
		subevents.add(subevent);
		eJson.put("subevents", subevents);

		e = new Event(eJson);
		source = new GameObject("test_gameobject_source", new Vector(), new Vector());

		e.invoke(source.getPos(), source);
		assertEquals((long) (22 - 3), (long) source.getHealth().get("current"));

		e.invoke(source.getPos(), source);
		assertEquals((long) (22 - 3 - 3), (long) source.getHealth().get("current"));
	}

}
