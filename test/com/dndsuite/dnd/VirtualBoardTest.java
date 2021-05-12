package com.dndsuite.dnd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

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

public class VirtualBoardTest {

	// Entity e;

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
		VirtualBoard.clearGameObjects();
	}

	@Test
	@DisplayName("Add & Remove GameObjects")
	@SuppressWarnings("unchecked")
	void test001() {
		JSONObject oJson = new JSONObject();
		JSONArray pos = new JSONArray();
		pos.add(10.0);
		pos.add(10.0);
		pos.add(10.0);
		oJson.put("pos", pos);
		JSONArray rot = new JSONArray();
		rot.add(1.0);
		rot.add(1.0);
		rot.add(1.0);
		oJson.put("rot", rot);
		GameObject o = new GameObject(oJson);

		VirtualBoard.addGameObject(o);
		assertTrue(VirtualBoard.contains(o));

		VirtualBoard.removeGameObject(o);
		assertFalse(VirtualBoard.contains(o));
	}

	@Test
	@DisplayName("Clear VirtualBoard GameObjects")
	@SuppressWarnings("unchecked")
	void test002() {
		JSONObject oJson = new JSONObject();
		JSONArray pos = new JSONArray();
		pos.add(10.0);
		pos.add(10.0);
		pos.add(10.0);
		oJson.put("pos", pos);
		JSONArray rot = new JSONArray();
		rot.add(1.0);
		rot.add(1.0);
		rot.add(1.0);
		oJson.put("rot", rot);
		GameObject o = new GameObject(oJson);

		VirtualBoard.addGameObject(o);
		assertTrue(VirtualBoard.contains(o));

		VirtualBoard.clearGameObjects();
		assertFalse(VirtualBoard.contains(o));
	}

	@Test
	@DisplayName("nearestObject")
	@SuppressWarnings("unchecked")
	void test003() {
		JSONObject oJson;
		JSONArray pos;
		JSONArray rot;
		GameObject o;

		oJson = new JSONObject();
		pos = new JSONArray();
		pos.add(10.0);
		pos.add(10.0);
		pos.add(10.0);
		oJson.put("pos", pos);
		rot = new JSONArray();
		rot.add(1.0);
		rot.add(1.0);
		rot.add(1.0);
		oJson.put("rot", rot);
		o = new GameObject(oJson);
		VirtualBoard.addGameObject(o);

		oJson = new JSONObject();
		pos = new JSONArray();
		pos.add(20.0);
		pos.add(20.0);
		pos.add(20.0);
		oJson.put("pos", pos);
		rot = new JSONArray();
		rot.add(1.0);
		rot.add(1.0);
		rot.add(1.0);
		oJson.put("rot", rot);
		o = new GameObject(oJson);
		VirtualBoard.addGameObject(o);

		assertEquals(o, VirtualBoard.nearestObject(new Vector(20.0, 20.0, 20.0), new String[] {}));
		assertEquals(o, VirtualBoard.nearestObject(new Vector(21.0, 20.0, 20.0), new String[] {}));
		assertNotEquals(o, VirtualBoard.nearestObject(new Vector(10.0, 10.0, 10.0), new String[] {}));
	}

	@Test
	@DisplayName("objectsInCone")
	@SuppressWarnings("unchecked")
	void test004() {
		JSONObject oJson;
		JSONArray pos;
		JSONArray rot;
		GameObject o;

		double length = 10.0;
		ArrayList<GameObject> fullList = new ArrayList<GameObject>();

		for (int i = -(int) length - 1; i <= (int) length + 1; i++) {
			for (int k = -(int) length - 1; k <= (int) length + 1; k++) {
				oJson = new JSONObject();
				pos = new JSONArray();
				pos.add((double) i);
				pos.add(0.0);
				pos.add((double) k);
				oJson.put("pos", pos);
				rot = new JSONArray();
				rot.add(1.0);
				rot.add(1.0);
				rot.add(1.0);
				oJson.put("rot", rot);
				o = new GameObject(oJson);
				VirtualBoard.addGameObject(o);
				fullList.add(o);
			}
		}

		// A GameObject located at the source should not be returned by entitiesInCone()
		ArrayList<GameObject> list = VirtualBoard.objectsInCone(new Vector(0, 0, 0), new Vector(1, 0, 0), length,
				new String[] {});

		for (GameObject e : fullList) {
			if (e.getPos().x() >= 0 && e.getPos().x() <= length
					&& Math.abs(e.getPos().z()) <= Math.sin(Math.toRadians(22.5)) * e.getPos().x()
					&& !e.getPos().equalTo(new Vector(0, 0, 0))) {
				assertTrue(list.contains(e));
			} else {
				assertFalse(list.contains(e));
			}
		}
	}

	@Test
	@DisplayName("objectsInCube")
	@SuppressWarnings("unchecked")
	void test005() {
		JSONObject oJson;
		JSONArray pos;
		JSONArray rot;
		GameObject o;

		double radius = 10.0;
		ArrayList<GameObject> fullList = new ArrayList<GameObject>();

		for (int i = -(int) radius - 1; i <= (int) radius + 1; i++) {
			for (int k = -(int) radius - 1; k <= (int) radius + 1; k++) {
				oJson = new JSONObject();
				pos = new JSONArray();
				pos.add((double) i);
				pos.add(0.0);
				pos.add((double) k);
				oJson.put("pos", pos);
				rot = new JSONArray();
				rot.add(1.0);
				rot.add(1.0);
				rot.add(1.0);
				oJson.put("rot", rot);
				o = new GameObject(oJson);
				VirtualBoard.addGameObject(o);
				fullList.add(o);
			}
		}

		ArrayList<GameObject> list = VirtualBoard.objectsInCube(new Vector(0, 0, 0), new Vector(1, 0, 0), radius,
				new String[] {});

		for (GameObject e : fullList) {
			if (Math.abs(e.getPos().x()) <= radius && Math.abs(e.getPos().z()) <= radius) {
				assertTrue(list.contains(e));
			} else {
				assertFalse(list.contains(e));
			}
		}
	}

	@Test
	@DisplayName("objectsInLine")
	@SuppressWarnings("unchecked")
	void test006() {
		JSONObject oJson;
		JSONArray pos;
		JSONArray rot;
		GameObject o;

		double length = 10.0;
		double radius = 5.0;
		ArrayList<GameObject> fullList = new ArrayList<GameObject>();

		for (int i = -(int) length - 1; i <= (int) length + 1; i++) {
			for (int k = -(int) length - 1; k <= (int) length + 1; k++) {
				oJson = new JSONObject();
				pos = new JSONArray();
				pos.add((double) i);
				pos.add(0.0);
				pos.add((double) k);
				oJson.put("pos", pos);
				rot = new JSONArray();
				rot.add(1.0);
				rot.add(1.0);
				rot.add(1.0);
				oJson.put("rot", rot);
				o = new GameObject(oJson);
				VirtualBoard.addGameObject(o);
				fullList.add(o);
			}
		}

		// A GameObject, located at the source should not be returned by
		// entitiesInLine()
		ArrayList<GameObject> list = VirtualBoard.objectsInLine(new Vector(0, 0, 0), new Vector(1, 0, 0), length,
				radius, new String[] {});

		for (GameObject e : fullList) {
			if (e.getPos().x() >= 0 && e.getPos().x() <= length && Math.abs(e.getPos().z()) <= radius
					&& !e.getPos().equalTo(new Vector(0, 0, 0))) {
				assertTrue(list.contains(e));
			} else {
				assertFalse(list.contains(e));
			}
		}
	}

	@Test
	@DisplayName("objectsInSphere")
	@SuppressWarnings("unchecked")
	void test007() {
		JSONObject oJson;
		JSONArray pos;
		JSONArray rot;
		GameObject o;

		double radius = 10.0;
		ArrayList<GameObject> fullList = new ArrayList<GameObject>();

		for (int i = -(int) radius - 1; i <= (int) radius + 1; i++) {
			for (int k = -(int) radius - 1; k <= (int) radius + 1; k++) {
				oJson = new JSONObject();
				pos = new JSONArray();
				pos.add((double) i);
				pos.add(0.0);
				pos.add((double) k);
				oJson.put("pos", pos);
				rot = new JSONArray();
				rot.add(1.0);
				rot.add(1.0);
				rot.add(1.0);
				oJson.put("rot", rot);
				o = new GameObject(oJson);
				VirtualBoard.addGameObject(o);
				fullList.add(o);
			}
		}

		ArrayList<GameObject> list = VirtualBoard.objectsInSphere(new Vector(0, 0, 0), radius, new String[] {});

		for (GameObject e : fullList) {
			if (e.getPos().mag() <= radius) {
				assertTrue(list.contains(e));
			} else {
				assertFalse(list.contains(e));
			}
		}
	}

	@Test
	@DisplayName("objectsInAreaOfEffect")
	@SuppressWarnings("unchecked")
	void test008() {
		JSONObject eJson;
		JSONObject areaOfEffect;

		// TODO: complete this test case

		// cone
		eJson = new JSONObject();
		areaOfEffect = new JSONObject();
		areaOfEffect.put("shape", "cone");
		areaOfEffect.put("length", 10.0);
		eJson.put("area_of_effect", areaOfEffect);

		// cube
		eJson = new JSONObject();
		areaOfEffect = new JSONObject();
		areaOfEffect.put("shape", "cube");
		areaOfEffect.put("radius", 10.0);
		eJson.put("area_of_effect", areaOfEffect);

		// line
		eJson = new JSONObject();
		areaOfEffect = new JSONObject();
		areaOfEffect.put("shape", "line");
		areaOfEffect.put("length", 10.0);
		areaOfEffect.put("radius", 5.0);
		eJson.put("area_of_effect", areaOfEffect);

		// single target, self
		eJson = new JSONObject();
		areaOfEffect = new JSONObject();
		areaOfEffect.put("shape", "single_target");
		areaOfEffect.put("range", "self");
		eJson.put("area_of_effect", areaOfEffect);

		// single target, ranged
		eJson = new JSONObject();
		areaOfEffect = new JSONObject();
		areaOfEffect.put("shape", "single_target");
		areaOfEffect.put("range", 10.0);
		eJson.put("area_of_effect", areaOfEffect);

		// single target, long-ranged
		eJson = new JSONObject();
		areaOfEffect = new JSONObject();
		areaOfEffect.put("shape", "single_target");
		JSONObject range = new JSONObject();
		range.put("short", 10.0);
		range.put("long", 20.0);
		areaOfEffect.put("range", range);
		eJson.put("area_of_effect", areaOfEffect);

		// sphere
		eJson = new JSONObject();
		areaOfEffect = new JSONObject();
		areaOfEffect.put("shape", "cone");
		areaOfEffect.put("length", 10.0);
		eJson.put("area_of_effect", areaOfEffect);

	}

}
