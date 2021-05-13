package com.dndsuite.dnd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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
import com.dndsuite.exceptions.InvalidAreaOfEffectException;
import com.dndsuite.exceptions.OutOfRangeException;
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
	@DisplayName("objectsInAreaOfEffect cone range=self")
	@SuppressWarnings("unchecked")
	void test008() {
		JSONObject oJson;
		JSONArray pos;
		JSONArray rot;

		double length = 10.0;

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
				VirtualBoard.addGameObject(new GameObject(oJson));
			}
		}

		JSONObject eJson;
		JSONObject areaOfEffect;

		eJson = new JSONObject();
		areaOfEffect = new JSONObject();
		areaOfEffect.put("shape", "cone");
		areaOfEffect.put("range", "self");
		areaOfEffect.put("length", length);
		eJson.put("area_of_effect", areaOfEffect);

		GameObject source = VirtualBoard.nearestObject(new Vector(), new String[] {});
		Vector start = new Vector(1, 0, 1);
		Vector end = new Vector(5, 0, 5);
		ArrayList<GameObject> desired = VirtualBoard.objectsInCone(source.getPos(), end, length, new String[] {});
		try {
			ArrayList<GameObject> found = VirtualBoard.objectsInAreaOfEffect(source.getPos(), start, end, eJson);
			assertTrue(found.size() > 0);
			assertTrue(found.containsAll(desired));
			assertTrue(desired.containsAll(found));
		} catch (InvalidAreaOfEffectException ex) {
			ex.printStackTrace();
			fail("Invalid area_of_effect structure");
		} catch (OutOfRangeException ex) {
			ex.printStackTrace();
			fail("Unexpected out of range error");
		}
	}

	@Test
	@DisplayName("objectsInAreaOfEffect cone range=double")
	@SuppressWarnings("unchecked")
	void test009() {
		JSONObject oJson;
		JSONArray pos;
		JSONArray rot;

		double length = 10.0;

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
				VirtualBoard.addGameObject(new GameObject(oJson));
			}
		}

		JSONObject eJson;
		JSONObject areaOfEffect;
		GameObject source;
		Vector start;
		Vector end;
		ArrayList<GameObject> desired;

		eJson = new JSONObject();
		areaOfEffect = new JSONObject();
		areaOfEffect.put("shape", "cone");
		areaOfEffect.put("range", 5.0);
		areaOfEffect.put("length", length);
		eJson.put("area_of_effect", areaOfEffect);

		// within range

		source = VirtualBoard.nearestObject(new Vector(), new String[] {});
		start = new Vector(1, 0, 1);
		end = new Vector(5, 0, 5);
		desired = VirtualBoard.objectsInCone(start, end, length, new String[] {});
		try {
			ArrayList<GameObject> found = VirtualBoard.objectsInAreaOfEffect(source.getPos(), start, end, eJson);
			assertTrue(found.size() > 0);
			assertTrue(found.containsAll(desired));
			assertTrue(desired.containsAll(found));
		} catch (InvalidAreaOfEffectException ex) {
			ex.printStackTrace();
			fail("Invalid area_of_effect structure");
		} catch (OutOfRangeException ex) {
			ex.printStackTrace();
			fail("Unexpected out of range error");
		}

		// beyond range

		start = new Vector(7, 0, 7);
		desired = VirtualBoard.objectsInCone(start, end, length, new String[] {});
		try {
			VirtualBoard.objectsInAreaOfEffect(source.getPos(), start, end, eJson);
			fail("This line should not be reached");
		} catch (InvalidAreaOfEffectException ex) {
			ex.printStackTrace();
			fail("Invalid area_of_effect structure");
		} catch (OutOfRangeException ex) {
			// this exception is expected here
			assertTrue(true);
		}
	}

	@Test
	@DisplayName("objectsInAreaOfEffect cube range=self")
	@SuppressWarnings("unchecked")
	void test00A() {
		JSONObject oJson;
		JSONArray pos;
		JSONArray rot;

		double radius = 10.0;

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
				VirtualBoard.addGameObject(new GameObject(oJson));
			}
		}

		JSONObject eJson;
		JSONObject areaOfEffect;
		GameObject source;
		Vector start;
		Vector end;
		ArrayList<GameObject> desired;

		eJson = new JSONObject();
		areaOfEffect = new JSONObject();
		areaOfEffect.put("shape", "cube");
		areaOfEffect.put("range", "self");
		areaOfEffect.put("radius", radius);
		eJson.put("area_of_effect", areaOfEffect);

		source = VirtualBoard.nearestObject(new Vector(), new String[] {});
		start = new Vector(1, 0, 1);
		end = new Vector(5, 0, 5);
		desired = VirtualBoard.objectsInCube(source.getPos(), end, radius, new String[] {});
		try {
			ArrayList<GameObject> found = VirtualBoard.objectsInAreaOfEffect(source.getPos(), start, end, eJson);
			assertTrue(found.size() > 0);
			assertTrue(found.containsAll(desired));
			assertTrue(desired.containsAll(found));
		} catch (InvalidAreaOfEffectException ex) {
			ex.printStackTrace();
			fail("Invalid area_of_effect structure");
		} catch (OutOfRangeException ex) {
			ex.printStackTrace();
			fail("Unexpected out of range error");
		}
	}

	@Test
	@DisplayName("objectsInAreaOfEffect cube range=adjacent")
	@SuppressWarnings("unchecked")
	void test00B() {
		JSONObject oJson;
		JSONArray pos;
		JSONArray rot;

		double radius = 10.0;

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
				VirtualBoard.addGameObject(new GameObject(oJson));
			}
		}

		JSONObject eJson;
		JSONObject areaOfEffect;
		GameObject source;
		Vector start;
		Vector end;
		ArrayList<GameObject> desired;

		eJson = new JSONObject();
		areaOfEffect = new JSONObject();
		areaOfEffect.put("shape", "cube");
		areaOfEffect.put("range", "adjacent");
		areaOfEffect.put("radius", radius);
		eJson.put("area_of_effect", areaOfEffect);

		source = VirtualBoard.nearestObject(new Vector(), new String[] {});
		start = new Vector(1, 0, 1);
		end = new Vector(5, 0, 5);
		Vector center = end.sub(source.getPos()).unit().scale(radius);
		desired = VirtualBoard.objectsInCube(center, end, radius, new String[] {});
		try {
			ArrayList<GameObject> found = VirtualBoard.objectsInAreaOfEffect(source.getPos(), start, end, eJson);
			assertTrue(found.size() > 0);
			assertTrue(found.containsAll(desired));
			assertTrue(desired.containsAll(found));
		} catch (InvalidAreaOfEffectException ex) {
			ex.printStackTrace();
			fail("Invalid area_of_effect structure");
		} catch (OutOfRangeException ex) {
			ex.printStackTrace();
			fail("Unexpected out of range error");
		}
	}

	@Test
	@DisplayName("objectsInAreaOfEffect cube range=double")
	@SuppressWarnings("unchecked")
	void test00C() {
		JSONObject oJson;
		JSONArray pos;
		JSONArray rot;

		double radius = 10.0;

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
				VirtualBoard.addGameObject(new GameObject(oJson));
			}
		}

		JSONObject eJson;
		JSONObject areaOfEffect;
		GameObject source;
		Vector start;
		Vector end;
		ArrayList<GameObject> desired;

		eJson = new JSONObject();
		areaOfEffect = new JSONObject();
		areaOfEffect.put("shape", "cube");
		areaOfEffect.put("range", 3.0);
		areaOfEffect.put("radius", radius);
		eJson.put("area_of_effect", areaOfEffect);

		// within range

		source = VirtualBoard.nearestObject(new Vector(), new String[] {});
		start = new Vector(1, 0, 1);
		end = new Vector(5, 0, 5);
		desired = VirtualBoard.objectsInCube(start, end, radius, new String[] {});
		try {
			ArrayList<GameObject> found = VirtualBoard.objectsInAreaOfEffect(source.getPos(), start, end, eJson);
			assertTrue(found.size() > 0);
			assertTrue(found.containsAll(desired));
			assertTrue(desired.containsAll(found));
		} catch (InvalidAreaOfEffectException ex) {
			ex.printStackTrace();
			fail("Invalid area_of_effect structure");
		} catch (OutOfRangeException ex) {
			ex.printStackTrace();
			fail("Unexpected out of range error");
		}

		// beyond range

		source = VirtualBoard.nearestObject(new Vector(), new String[] {});
		start = new Vector(7, 0, 7);
		end = new Vector(5, 0, 5);
		desired = VirtualBoard.objectsInCube(start, end, radius, new String[] {});
		try {
			VirtualBoard.objectsInAreaOfEffect(source.getPos(), start, end, eJson);
			fail("This line should not be reached");
		} catch (InvalidAreaOfEffectException ex) {
			ex.printStackTrace();
			fail("Invalid area_of_effect structure");
		} catch (OutOfRangeException ex) {
			// this exception is expected here
			assertTrue(true);
		}
	}

	@Test
	@DisplayName("objectsInAreaOfEffect line range=self")
	@SuppressWarnings("unchecked")
	void test00D() {
		JSONObject oJson;
		JSONArray pos;
		JSONArray rot;

		double length = 10.0;
		double radius = 5.0;

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
				VirtualBoard.addGameObject(new GameObject(oJson));
			}
		}

		JSONObject eJson;
		JSONObject areaOfEffect;
		GameObject source;
		Vector start;
		Vector end;
		ArrayList<GameObject> desired;

		eJson = new JSONObject();
		areaOfEffect = new JSONObject();
		areaOfEffect.put("shape", "line");
		areaOfEffect.put("range", "self");
		areaOfEffect.put("radius", radius);
		areaOfEffect.put("length", length);
		eJson.put("area_of_effect", areaOfEffect);

		source = VirtualBoard.nearestObject(new Vector(), new String[] {});
		start = new Vector(1, 0, 1);
		end = new Vector(5, 0, 5);
		desired = VirtualBoard.objectsInLine(source.getPos(), end, length, radius, new String[] {});
		try {
			ArrayList<GameObject> found = VirtualBoard.objectsInAreaOfEffect(source.getPos(), start, end, eJson);
			assertTrue(found.size() > 0);
			assertTrue(found.containsAll(desired));
			assertTrue(desired.containsAll(found));
		} catch (InvalidAreaOfEffectException ex) {
			ex.printStackTrace();
			fail("Invalid area_of_effect structure");
		} catch (OutOfRangeException ex) {
			ex.printStackTrace();
			fail("Unexpected out of range error");
		}
	}

	@Test
	@DisplayName("objectsInAreaOfEffect line range=double")
	@SuppressWarnings("unchecked")
	void test00E() {
		JSONObject oJson;
		JSONArray pos;
		JSONArray rot;

		double length = 10.0;
		double radius = 5.0;

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
				VirtualBoard.addGameObject(new GameObject(oJson));
			}
		}

		JSONObject eJson;
		JSONObject areaOfEffect;
		GameObject source;
		Vector start;
		Vector end;
		ArrayList<GameObject> desired;

		eJson = new JSONObject();
		areaOfEffect = new JSONObject();
		areaOfEffect.put("shape", "line");
		areaOfEffect.put("range", 5.0);
		areaOfEffect.put("radius", radius);
		areaOfEffect.put("length", length);
		eJson.put("area_of_effect", areaOfEffect);

		// within range

		source = VirtualBoard.nearestObject(new Vector(), new String[] {});
		start = new Vector(1, 0, 1);
		end = new Vector(5, 0, 5);
		desired = VirtualBoard.objectsInLine(start, end, length, radius, new String[] {});
		try {
			ArrayList<GameObject> found = VirtualBoard.objectsInAreaOfEffect(source.getPos(), start, end, eJson);
			assertTrue(found.size() > 0);
			assertTrue(found.containsAll(desired));
			assertTrue(desired.containsAll(found));
		} catch (InvalidAreaOfEffectException ex) {
			ex.printStackTrace();
			fail("Invalid area_of_effect structure");
		} catch (OutOfRangeException ex) {
			ex.printStackTrace();
			fail("Unexpected out of range error");
		}

		// beyond range

		start = new Vector(7, 0, 7);
		desired = VirtualBoard.objectsInLine(start, end, length, radius, new String[] {});
		try {
			VirtualBoard.objectsInAreaOfEffect(source.getPos(), start, end, eJson);
			fail("This line should not be reached");
		} catch (InvalidAreaOfEffectException ex) {
			ex.printStackTrace();
			fail("Invalid area_of_effect structure");
		} catch (OutOfRangeException ex) {
			// this exception is expected here
			assertTrue(true);
		}
	}

	@Test
	@DisplayName("objectsInAreaOfEffect single_target range=self")
	@SuppressWarnings("unchecked")
	void test00F() {
		JSONObject oJson;
		JSONArray pos;
		JSONArray rot;

		double length = 10.0;

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
				VirtualBoard.addGameObject(new GameObject(oJson));
			}
		}

		JSONObject eJson;
		JSONObject areaOfEffect;
		GameObject source;
		Vector start;
		Vector end;
		ArrayList<GameObject> desired = new ArrayList<GameObject>();

		eJson = new JSONObject();
		areaOfEffect = new JSONObject();
		areaOfEffect.put("shape", "single_target");
		areaOfEffect.put("range", "self");
		eJson.put("area_of_effect", areaOfEffect);

		source = VirtualBoard.nearestObject(new Vector(), new String[] {});
		start = new Vector(1, 0, 1);
		end = new Vector(5, 0, 5);
		desired.add(VirtualBoard.nearestObject(source.getPos(), new String[] {}));
		try {
			ArrayList<GameObject> found = VirtualBoard.objectsInAreaOfEffect(source.getPos(), start, end, eJson);
			assertTrue(found.size() == 1);
			assertTrue(found.containsAll(desired));
			assertTrue(desired.containsAll(found));
		} catch (InvalidAreaOfEffectException ex) {
			ex.printStackTrace();
			fail("Invalid area_of_effect structure");
		} catch (OutOfRangeException ex) {
			ex.printStackTrace();
			fail("Unexpected out of range error");
		}
	}

	@Test
	@DisplayName("objectsInAreaOfEffect single_target range=short/long")
	@SuppressWarnings("unchecked")
	void test010() {
		JSONObject oJson;
		JSONArray pos;
		JSONArray rot;

		double length = 10.0;

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
				VirtualBoard.addGameObject(new GameObject(oJson));
			}
		}

		JSONObject eJson;
		JSONObject areaOfEffect;
		GameObject source;
		Vector start;
		Vector end;
		ArrayList<GameObject> desired = new ArrayList<GameObject>();

		eJson = new JSONObject();
		areaOfEffect = new JSONObject();
		areaOfEffect.put("shape", "single_target");
		JSONObject range = new JSONObject();
		range.put("short", 5.0);
		range.put("long", 10.0);
		areaOfEffect.put("range", range);
		eJson.put("area_of_effect", areaOfEffect);

		// within range

		source = VirtualBoard.nearestObject(new Vector(), new String[] {});
		start = new Vector(1, 0, 1);
		end = new Vector(5, 0, 5);
		desired.add(VirtualBoard.nearestObject(end, new String[] {}));
		try {
			ArrayList<GameObject> found = VirtualBoard.objectsInAreaOfEffect(source.getPos(), start, end, eJson);
			assertTrue(found.size() == 1);
			assertTrue(found.containsAll(desired));
			assertTrue(desired.containsAll(found));
		} catch (InvalidAreaOfEffectException ex) {
			ex.printStackTrace();
			fail("Invalid area_of_effect structure");
		} catch (OutOfRangeException ex) {
			ex.printStackTrace();
			fail("Unexpected out of range error");
		}

		// beyond range

		end = new Vector(10, 0, 10);
		desired.clear();
		desired.add(VirtualBoard.nearestObject(end, new String[] {}));
		try {
			VirtualBoard.objectsInAreaOfEffect(source.getPos(), start, end, eJson);
			fail("This line should not be reached");
		} catch (InvalidAreaOfEffectException ex) {
			ex.printStackTrace();
			fail("Invalid area_of_effect structure");
		} catch (OutOfRangeException ex) {
			// this exception is expected here
			assertTrue(true);
		}
	}

	@Test
	@DisplayName("objectsInAreaOfEffect single_target range=double")
	@SuppressWarnings("unchecked")
	void test011() {
		JSONObject oJson;
		JSONArray pos;
		JSONArray rot;

		double length = 10.0;

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
				VirtualBoard.addGameObject(new GameObject(oJson));
			}
		}

		JSONObject eJson;
		JSONObject areaOfEffect;
		GameObject source;
		Vector start;
		Vector end;
		ArrayList<GameObject> desired = new ArrayList<GameObject>();

		eJson = new JSONObject();
		areaOfEffect = new JSONObject();
		areaOfEffect.put("shape", "single_target");
		areaOfEffect.put("range", 10.0);
		eJson.put("area_of_effect", areaOfEffect);

		// within range

		source = VirtualBoard.nearestObject(new Vector(), new String[] {});
		start = new Vector(1, 0, 1);
		end = new Vector(5, 0, 5);
		desired.add(VirtualBoard.nearestObject(end, new String[] {}));
		try {
			ArrayList<GameObject> found = VirtualBoard.objectsInAreaOfEffect(source.getPos(), start, end, eJson);
			assertTrue(found.size() == 1);
			assertTrue(found.containsAll(desired));
			assertTrue(desired.containsAll(found));
		} catch (InvalidAreaOfEffectException ex) {
			ex.printStackTrace();
			fail("Invalid area_of_effect structure");
		} catch (OutOfRangeException ex) {
			ex.printStackTrace();
			fail("Unexpected out of range error");
		}

		// beyond range

		end = new Vector(10, 0, 10);
		desired.clear();
		desired.add(VirtualBoard.nearestObject(end, new String[] {}));
		try {
			VirtualBoard.objectsInAreaOfEffect(source.getPos(), start, end, eJson);
			fail("This line should not be reached");
		} catch (InvalidAreaOfEffectException ex) {
			ex.printStackTrace();
			fail("Invalid area_of_effect structure");
		} catch (OutOfRangeException ex) {
			// this exception is expected here
			assertTrue(true);
		}
	}

	@Test
	@DisplayName("objectsInAreaOfEffect sphere range=self")
	@SuppressWarnings("unchecked")
	void test012() {
		JSONObject oJson;
		JSONArray pos;
		JSONArray rot;

		double length = 10.0;
		double radius = 5.0;

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
				VirtualBoard.addGameObject(new GameObject(oJson));
			}
		}

		JSONObject eJson;
		JSONObject areaOfEffect;
		GameObject source;
		Vector start;
		Vector end;
		ArrayList<GameObject> desired;

		eJson = new JSONObject();
		areaOfEffect = new JSONObject();
		areaOfEffect.put("shape", "sphere");
		areaOfEffect.put("range", "self");
		areaOfEffect.put("radius", radius);
		eJson.put("area_of_effect", areaOfEffect);

		source = VirtualBoard.nearestObject(new Vector(), new String[] {});
		start = new Vector(1, 0, 1);
		end = new Vector(5, 0, 5);
		desired = VirtualBoard.objectsInSphere(source.getPos(), radius, new String[] {});
		try {
			ArrayList<GameObject> found = VirtualBoard.objectsInAreaOfEffect(source.getPos(), start, end, eJson);
			assertTrue(found.size() > 0);
			assertTrue(found.containsAll(desired));
			assertTrue(desired.containsAll(found));
		} catch (InvalidAreaOfEffectException ex) {
			ex.printStackTrace();
			fail("Invalid area_of_effect structure");
		} catch (OutOfRangeException ex) {
			ex.printStackTrace();
			fail("Unexpected out of range error");
		}
	}

	@Test
	@DisplayName("objectsInAreaOfEffect sphere range=double")
	@SuppressWarnings("unchecked")
	void test013() {
		JSONObject oJson;
		JSONArray pos;
		JSONArray rot;

		double length = 10.0;
		double radius = 5.0;

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
				VirtualBoard.addGameObject(new GameObject(oJson));
			}
		}

		JSONObject eJson;
		JSONObject areaOfEffect;
		GameObject source;
		Vector start;
		Vector end;
		ArrayList<GameObject> desired;

		eJson = new JSONObject();
		areaOfEffect = new JSONObject();
		areaOfEffect.put("shape", "sphere");
		areaOfEffect.put("range", 4.0);
		areaOfEffect.put("radius", radius);
		eJson.put("area_of_effect", areaOfEffect);

		// within range

		source = VirtualBoard.nearestObject(new Vector(), new String[] {});
		start = new Vector(1, 0, 1);
		end = new Vector(5, 0, 5);
		desired = VirtualBoard.objectsInSphere(start, radius, new String[] {});
		try {
			ArrayList<GameObject> found = VirtualBoard.objectsInAreaOfEffect(source.getPos(), start, end, eJson);
			assertTrue(found.size() > 0);
			assertTrue(found.containsAll(desired));
			assertTrue(desired.containsAll(found));
		} catch (InvalidAreaOfEffectException ex) {
			ex.printStackTrace();
			fail("Invalid area_of_effect structure");
		} catch (OutOfRangeException ex) {
			ex.printStackTrace();
			fail("Unexpected out of range error");
		}

		// beyond range

		start = new Vector(7, 0, 7);
		desired = VirtualBoard.objectsInSphere(start, radius, new String[] {});
		try {
			VirtualBoard.objectsInAreaOfEffect(source.getPos(), start, end, eJson);
			fail("This line should not be reached");
		} catch (InvalidAreaOfEffectException ex) {
			ex.printStackTrace();
			fail("Invalid area_of_effect structure");
		} catch (OutOfRangeException ex) {
			// this exception is expected here
			assertTrue(true);
		}
	}

}
