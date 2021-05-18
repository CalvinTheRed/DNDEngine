package com.dndsuite.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.dndsuite.exceptions.InvalidAreaOfEffectException;
import com.dndsuite.exceptions.OutOfRangeException;
import com.dndsuite.maths.Vector;

public class VirtualBoardTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		int testGridSideLength = 15;
		for (int i = -testGridSideLength; i <= testGridSideLength; i++) {
			for (int k = -testGridSideLength; k <= testGridSideLength; k++) {
				JSONParser parser = new JSONParser();
				String jsonString = "{\"pos\":[" + (double) i + ",0.0," + (double) k + "]}";
				JSONObject gameObjectJson = (JSONObject) parser.parse(jsonString);
				// Constructing a GameObject adds it to the VirtualBoard automatically
				new GameObject(gameObjectJson);
			}
		}
	}

	@AfterEach
	void tearDown() throws Exception {
		VirtualBoard.clearGameObjects();
	}

	@Test
	@DisplayName("Add & Remove GameObjects")
	void test001() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString = "{\"pos\":[0.0,0.0,0.0]}";
			JSONObject gameObjectJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(gameObjectJson);

			assertTrue(VirtualBoard.contains(dummy));

			VirtualBoard.removeGameObject(dummy);
			assertFalse(VirtualBoard.contains(dummy));

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parser error");
		}
	}

	@Test
	@DisplayName("Clear VirtualBoard GameObjects")
	void test002() {
		try {
			JSONParser parser = new JSONParser();
			String jsonString = "{\"pos\":[0.0,0.0,0.0]}";
			JSONObject gameObjectJson = (JSONObject) parser.parse(jsonString);
			GameObject dummy = new GameObject(gameObjectJson);

			assertTrue(VirtualBoard.contains(dummy));

			VirtualBoard.clearGameObjects();
			assertFalse(VirtualBoard.contains(dummy));

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parser error");
		}
	}

	@Test
	@DisplayName("nearestObject")
	void test003() {
		GameObject objectAt_0_0_0 = null;
		GameObject objectAt_1_0_0 = null;

		for (GameObject o : VirtualBoard.getGameObjects()) {
			if (o.getPos().equalTo(new Vector(0.0, 0.0, 0.0))) {
				objectAt_0_0_0 = o;
			} else if (o.getPos().equalTo(new Vector(1.0, 0.0, 0.0))) {
				objectAt_1_0_0 = o;
			}
		}

		assertEquals(objectAt_0_0_0, VirtualBoard.nearestObject(new Vector(0.0, 0.0, 0.0), new String[] {}));
		assertEquals(objectAt_1_0_0, VirtualBoard.nearestObject(new Vector(1.0, 0.0, 0.0), new String[] {}));
	}

	@Test
	@DisplayName("objectsInCone")
	void test004() {
		Vector startPos = new Vector(0.0, 0.0, 0.0);
		Vector pointTo = new Vector(1.0, 0.0, 0.0);
		double length = 8.0;

		// GameObject located at vertex not to be returned by area search
		ArrayList<GameObject> list = VirtualBoard.objectsInCone(startPos, pointTo, length, new String[] {});

		for (GameObject o : VirtualBoard.getGameObjects()) {
			if (o.getPos().x() >= 0 && o.getPos().x() <= length
					&& Math.abs(o.getPos().z()) <= Math.sin(Math.toRadians(22.5)) * o.getPos().x()
					&& !o.getPos().equalTo(startPos)) {
				assertTrue(list.contains(o));
			} else {
				assertFalse(list.contains(o));
			}
		}
	}

	@Test
	@DisplayName("objectsInCube")
	void test005() {
		Vector startPos = new Vector(0.0, 0.0, 0.0);
		Vector pointTo = new Vector(1.0, 0.0, 0.0);
		double radius = 6.0;

		ArrayList<GameObject> list = VirtualBoard.objectsInCube(startPos, pointTo, radius, new String[] {});

		for (GameObject e : VirtualBoard.getGameObjects()) {
			if (Math.abs(e.getPos().x()) <= radius && Math.abs(e.getPos().z()) <= radius) {
				assertTrue(list.contains(e));
			} else {
				assertFalse(list.contains(e));
			}
		}
	}

	@Test
	@DisplayName("objectsInLine")
	void test006() {
		Vector startPos = new Vector(0.0, 0.0, 0.0);
		Vector pointTo = new Vector(1.0, 0.0, 0.0);
		double length = 8.0;
		double radius = 5.0;

		// GameObject located at start position not to be returned by area search
		ArrayList<GameObject> list = VirtualBoard.objectsInLine(startPos, pointTo, length, radius, new String[] {});

		for (GameObject e : VirtualBoard.getGameObjects()) {
			if (e.getPos().x() >= 0 && e.getPos().x() <= length && Math.abs(e.getPos().z()) <= radius
					&& !e.getPos().equalTo(startPos)) {
				assertTrue(list.contains(e));
			} else {
				assertFalse(list.contains(e));
			}
		}
	}

	@Test
	@DisplayName("objectsInSphere")
	void test007() {
		Vector startPos = new Vector(0.0, 0.0, 0.0);
		// there is no pointTo Vector for this area search, since all possible pointTo
		// values will result in an identical volume
		double radius = 6.0;

		ArrayList<GameObject> list = VirtualBoard.objectsInSphere(startPos, radius, new String[] {});

		for (GameObject e : VirtualBoard.getGameObjects()) {
			if (e.getPos().mag() <= radius) {
				assertTrue(list.contains(e));
			} else {
				assertFalse(list.contains(e));
			}
		}
	}

	@Test
	@DisplayName("objectsInAreaOfEffect cone range=self")
	void test008() {
		try {
			Vector sourcePos = new Vector(0.0, 0.0, 0.0);
			Vector startPos = new Vector(1.0, 0.0, 0.0);
			Vector pointTo = new Vector(2.0, 0.0, 0.0);
			double length = 10.0;

			JSONParser parser = new JSONParser();
			String jsonString = "{\"area_of_effect\":{\"shape\":\"cone\",\"range\":\"self\",\"length\":" + length
					+ "}}";
			JSONObject areaOfEffect = (JSONObject) parser.parse(jsonString);

			ArrayList<GameObject> desired = VirtualBoard.objectsInCone(sourcePos, pointTo, length, new String[] {});
			ArrayList<GameObject> found = VirtualBoard.objectsInAreaOfEffect(sourcePos, startPos, pointTo,
					areaOfEffect);
			assertTrue(found.size() > 0);
			assertTrue(found.containsAll(desired));
			assertTrue(desired.containsAll(found));

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parser exception");
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
	void test009() {
		try {
			Vector sourcePos;
			Vector startPos;
			Vector pointTo;
			double length = 10.0;
			double range = 10.0;

			JSONParser parser = new JSONParser();
			String jsonString = "{\"area_of_effect\":{\"shape\":\"cone\",\"range\":" + range + ",\"length\":" + length
					+ "}}";
			JSONObject areaOfEffect = (JSONObject) parser.parse(jsonString);

			ArrayList<GameObject> desired;
			ArrayList<GameObject> found;

			// within range
			sourcePos = new Vector(0.0, 0.0, 0.0);
			startPos = new Vector(1.0, 0.0, 0.0);
			pointTo = new Vector(2.0, 0.0, 0.0);

			desired = VirtualBoard.objectsInCone(startPos, pointTo, length, new String[] {});
			found = VirtualBoard.objectsInAreaOfEffect(sourcePos, startPos, pointTo, areaOfEffect);
			assertTrue(found.size() > 0);
			assertTrue(found.containsAll(desired));
			assertTrue(desired.containsAll(found));

			// beyond range
			sourcePos = new Vector(0.0, 0.0, 0.0);
			startPos = new Vector(range + 1.0, 0.0, 0.0);
			pointTo = new Vector(2.0, 0.0, 0.0);

			try {
				VirtualBoard.objectsInAreaOfEffect(sourcePos, startPos, pointTo, areaOfEffect);
				fail("This line of code should not be reached");
			} catch (OutOfRangeException ex) {
				// expected exception
				assertTrue(true);
			}

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parser exception");
		} catch (InvalidAreaOfEffectException ex) {
			ex.printStackTrace();
			fail("Invalid area_of_effect structure");
		} catch (OutOfRangeException ex) {
			ex.printStackTrace();
			fail("Unexpected out of range error");
		}
	}

	@Test
	@DisplayName("objectsInAreaOfEffect cube range=self")
	void test00A() {
		try {
			Vector sourcePos = new Vector(0.0, 0.0, 0.0);
			Vector startPos = new Vector(1.0, 0.0, 0.0);
			Vector pointTo = new Vector(2.0, 0.0, 0.0);
			double radius = 8.0;

			JSONParser parser = new JSONParser();
			String jsonString = "{\"area_of_effect\":{\"shape\":\"cube\",\"range\":\"self\",\"radius\":" + radius
					+ "}}";
			JSONObject areaOfEffect = (JSONObject) parser.parse(jsonString);

			ArrayList<GameObject> desired = VirtualBoard.objectsInCube(sourcePos, pointTo, radius, new String[] {});
			ArrayList<GameObject> found = VirtualBoard.objectsInAreaOfEffect(sourcePos, startPos, pointTo,
					areaOfEffect);
			assertTrue(found.size() > 0);
			assertTrue(found.containsAll(desired));
			assertTrue(desired.containsAll(found));

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parser exception");
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
	void test00B() {
		try {
			Vector sourcePos = new Vector(0.0, 0.0, 0.0);
			Vector startPos = new Vector(1.0, 0.0, 0.0);
			Vector pointTo = new Vector(2.0, 0.0, 0.0);
			double radius = 8.0;

			Vector center = pointTo.sub(sourcePos).unit().scale(radius).add(sourcePos);

			JSONParser parser = new JSONParser();
			String jsonString = "{\"area_of_effect\":{\"shape\":\"cube\",\"range\":\"adjacent\",\"radius\":" + radius
					+ "}}";
			JSONObject areaOfEffect = (JSONObject) parser.parse(jsonString);

			ArrayList<GameObject> desired = VirtualBoard.objectsInCube(center, pointTo, radius, new String[] {});
			ArrayList<GameObject> found = VirtualBoard.objectsInAreaOfEffect(sourcePos, startPos, pointTo,
					areaOfEffect);
			assertTrue(found.size() > 0);
			assertTrue(found.containsAll(desired));
			assertTrue(desired.containsAll(found));

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parser exception");
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
	void test00C() {
		try {
			Vector sourcePos;
			Vector startPos;
			Vector pointTo;
			double radius = 10.0;
			double range = 10.0;

			JSONParser parser = new JSONParser();
			String jsonString = "{\"area_of_effect\":{\"shape\":\"cube\",\"range\":" + range + ",\"radius\":" + radius
					+ "}}";
			JSONObject areaOfEffect = (JSONObject) parser.parse(jsonString);

			ArrayList<GameObject> desired;
			ArrayList<GameObject> found;

			// within range
			sourcePos = new Vector(0.0, 0.0, 0.0);
			startPos = new Vector(1.0, 0.0, 0.0);
			pointTo = new Vector(2.0, 0.0, 0.0);

			desired = VirtualBoard.objectsInCube(startPos, pointTo, radius, new String[] {});
			found = VirtualBoard.objectsInAreaOfEffect(sourcePos, startPos, pointTo, areaOfEffect);
			assertTrue(found.size() > 0);
			assertTrue(found.containsAll(desired));
			assertTrue(desired.containsAll(found));

			// beyond range
			sourcePos = new Vector(0.0, 0.0, 0.0);
			startPos = new Vector(range + 1.0, 0.0, 0.0);
			pointTo = new Vector(2.0, 0.0, 0.0);

			try {
				VirtualBoard.objectsInAreaOfEffect(sourcePos, startPos, pointTo, areaOfEffect);
				fail("This line of code should not be reached");
			} catch (OutOfRangeException ex) {
				// expected exception
				assertTrue(true);
			}

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parser exception");
		} catch (InvalidAreaOfEffectException ex) {
			ex.printStackTrace();
			fail("Invalid area_of_effect structure");
		} catch (OutOfRangeException ex) {
			ex.printStackTrace();
			fail("Unexpected out of range error");
		}
	}

	@Test
	@DisplayName("objectsInAreaOfEffect line range=self")
	void test00D() {
		try {
			Vector sourcePos = new Vector(0.0, 0.0, 0.0);
			Vector startPos = new Vector(1.0, 0.0, 0.0);
			Vector pointTo = new Vector(2.0, 0.0, 0.0);
			double length = 10.0;
			double radius = 2.0;

			JSONParser parser = new JSONParser();
			String jsonString = "{\"area_of_effect\":{\"shape\":\"line\",\"range\":\"self\",\"length\":" + length
					+ ",\"radius\":" + radius + "}}";
			JSONObject areaOfEffect = (JSONObject) parser.parse(jsonString);

			ArrayList<GameObject> desired = VirtualBoard.objectsInLine(sourcePos, pointTo, length, radius,
					new String[] {});
			ArrayList<GameObject> found = VirtualBoard.objectsInAreaOfEffect(sourcePos, startPos, pointTo,
					areaOfEffect);
			assertTrue(found.size() > 0);
			assertTrue(found.containsAll(desired));
			assertTrue(desired.containsAll(found));

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parser exception");
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
	void test00E() {
		try {
			Vector sourcePos;
			Vector startPos;
			Vector pointTo;
			double length = 10.0;
			double radius = 2.0;
			double range = 10.0;

			JSONParser parser = new JSONParser();
			String jsonString = "{\"area_of_effect\":{\"shape\":\"line\",\"range\":" + range + ",\"length\":" + length
					+ ",\"radius\":" + radius + "}}";
			JSONObject areaOfEffect = (JSONObject) parser.parse(jsonString);

			ArrayList<GameObject> desired;
			ArrayList<GameObject> found;

			// within range
			sourcePos = new Vector(0.0, 0.0, 0.0);
			startPos = new Vector(1.0, 0.0, 0.0);
			pointTo = new Vector(2.0, 0.0, 0.0);

			desired = VirtualBoard.objectsInLine(startPos, pointTo, length, radius, new String[] {});
			found = VirtualBoard.objectsInAreaOfEffect(sourcePos, startPos, pointTo, areaOfEffect);
			assertTrue(found.size() > 0);
			assertTrue(found.containsAll(desired));
			assertTrue(desired.containsAll(found));

			// beyond range
			sourcePos = new Vector(0.0, 0.0, 0.0);
			startPos = new Vector(range + 1.0, 0.0, 0.0);
			pointTo = new Vector(2.0, 0.0, 0.0);

			try {
				VirtualBoard.objectsInAreaOfEffect(sourcePos, startPos, pointTo, areaOfEffect);
				fail("This line of code should not be reached");
			} catch (OutOfRangeException ex) {
				// expected exception
				assertTrue(true);
			}

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parser exception");
		} catch (InvalidAreaOfEffectException ex) {
			ex.printStackTrace();
			fail("Invalid area_of_effect structure");
		} catch (OutOfRangeException ex) {
			ex.printStackTrace();
			fail("Unexpected out of range error");
		}
	}

	@Test
	@DisplayName("objectsInAreaOfEffect single_target range=self")
	void test00F() {
		try {
			Vector sourcePos = new Vector(0.0, 0.0, 0.0);
			Vector startPos = new Vector(1.0, 0.0, 0.0);
			Vector pointTo = new Vector(2.0, 0.0, 0.0);

			JSONParser parser = new JSONParser();
			String jsonString = "{\"area_of_effect\":{\"shape\":\"single_target\",\"range\":\"self\"}}";
			JSONObject areaOfEffect = (JSONObject) parser.parse(jsonString);

			ArrayList<GameObject> desired = new ArrayList<GameObject>();
			GameObject target = null;
			for (GameObject o : VirtualBoard.getGameObjects()) {
				if (o.getPos().equalTo(sourcePos)) {
					target = o;
				}
			}
			desired.add(target);
			ArrayList<GameObject> found = VirtualBoard.objectsInAreaOfEffect(sourcePos, startPos, pointTo,
					areaOfEffect);
			assertTrue(found.size() > 0);
			assertTrue(found.containsAll(desired));
			assertTrue(desired.containsAll(found));

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parser exception");
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
	void test010() {
		try {
			Vector sourcePos;
			Vector startPos;
			Vector pointTo;
			double shortrange = 5.0;
			double longrange = 10.0;

			JSONParser parser = new JSONParser();
			String jsonString = "{\"area_of_effect\":{\"shape\":\"single_target\",\"range\":{\"short\":" + shortrange
					+ ",\"long\":" + longrange + "}}}";
			JSONObject areaOfEffect = (JSONObject) parser.parse(jsonString);

			ArrayList<GameObject> desired;
			ArrayList<GameObject> found;

			// within range
			sourcePos = new Vector(0.0, 0.0, 0.0);
			startPos = new Vector(1.0, 0.0, 0.0);
			pointTo = new Vector(2.0, 0.0, 0.0);

			desired = new ArrayList<GameObject>();
			desired.add(VirtualBoard.nearestObject(startPos, new String[] {}));
			found = VirtualBoard.objectsInAreaOfEffect(sourcePos, startPos, pointTo, areaOfEffect);
			assertTrue(found.size() > 0);
			assertTrue(found.containsAll(desired));
			assertTrue(desired.containsAll(found));

			// beyond range
			sourcePos = new Vector(0.0, 0.0, 0.0);
			startPos = new Vector(longrange + 1.0, 0.0, 0.0);
			pointTo = new Vector(2.0, 0.0, 0.0);

			try {
				VirtualBoard.objectsInAreaOfEffect(sourcePos, startPos, pointTo, areaOfEffect);
				fail("This line of code should not be reached");
			} catch (OutOfRangeException ex) {
				// expected exception
				assertTrue(true);
			}

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parser exception");
		} catch (InvalidAreaOfEffectException ex) {
			ex.printStackTrace();
			fail("Invalid area_of_effect structure");
		} catch (OutOfRangeException ex) {
			ex.printStackTrace();
			fail("Unexpected out of range error");
		}
	}

	@Test
	@DisplayName("objectsInAreaOfEffect single_target range=double")
	void test011() {
		try {
			Vector sourcePos;
			Vector startPos;
			Vector pointTo;
			double range = 10.0;

			JSONParser parser = new JSONParser();
			String jsonString = "{\"area_of_effect\":{\"shape\":\"single_target\",\"range\":" + range + "}}";
			JSONObject areaOfEffect = (JSONObject) parser.parse(jsonString);

			ArrayList<GameObject> desired;
			ArrayList<GameObject> found;

			// within range
			sourcePos = new Vector(0.0, 0.0, 0.0);
			startPos = new Vector(1.0, 0.0, 0.0);
			pointTo = new Vector(2.0, 0.0, 0.0);

			desired = new ArrayList<GameObject>();
			desired.add(VirtualBoard.nearestObject(startPos, new String[] {}));
			found = VirtualBoard.objectsInAreaOfEffect(sourcePos, startPos, pointTo, areaOfEffect);
			assertTrue(found.size() > 0);
			assertTrue(found.containsAll(desired));
			assertTrue(desired.containsAll(found));

			// beyond range
			sourcePos = new Vector(0.0, 0.0, 0.0);
			startPos = new Vector(range + 1.0, 0.0, 0.0);
			pointTo = new Vector(2.0, 0.0, 0.0);

			try {
				VirtualBoard.objectsInAreaOfEffect(sourcePos, startPos, pointTo, areaOfEffect);
				fail("This line of code should not be reached");
			} catch (OutOfRangeException ex) {
				// expected exception
				assertTrue(true);
			}

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parser exception");
		} catch (InvalidAreaOfEffectException ex) {
			ex.printStackTrace();
			fail("Invalid area_of_effect structure");
		} catch (OutOfRangeException ex) {
			ex.printStackTrace();
			fail("Unexpected out of range error");
		}
	}

	@Test
	@DisplayName("objectsInAreaOfEffect sphere range=self")
	void test012() {
		try {
			Vector sourcePos = new Vector(0.0, 0.0, 0.0);
			Vector startPos = new Vector(1.0, 0.0, 0.0);
			Vector pointTo = new Vector(2.0, 0.0, 0.0);
			double radius = 8.0;

			JSONParser parser = new JSONParser();
			String jsonString = "{\"area_of_effect\":{\"shape\":\"sphere\",\"range\":\"self\",\"radius\":" + radius
					+ "}}";
			JSONObject areaOfEffect = (JSONObject) parser.parse(jsonString);

			ArrayList<GameObject> desired = VirtualBoard.objectsInSphere(sourcePos, radius, new String[] {});
			ArrayList<GameObject> found = VirtualBoard.objectsInAreaOfEffect(sourcePos, startPos, pointTo,
					areaOfEffect);
			assertTrue(found.size() > 0);
			assertTrue(found.containsAll(desired));
			assertTrue(desired.containsAll(found));

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parser exception");
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
	void test013() {
		try {
			Vector sourcePos;
			Vector startPos;
			Vector pointTo;
			double range = 10.0;
			double radius = 8.0;

			JSONParser parser = new JSONParser();
			String jsonString = "{\"area_of_effect\":{\"shape\":\"sphere\",\"range\":" + range + ",\"radius\":" + radius
					+ "}}";
			JSONObject areaOfEffect = (JSONObject) parser.parse(jsonString);

			ArrayList<GameObject> desired;
			ArrayList<GameObject> found;

			// within range
			sourcePos = new Vector(0.0, 0.0, 0.0);
			startPos = new Vector(1.0, 0.0, 0.0);
			pointTo = new Vector(2.0, 0.0, 0.0);

			desired = VirtualBoard.objectsInSphere(startPos, radius, new String[] {});
			found = VirtualBoard.objectsInAreaOfEffect(sourcePos, startPos, pointTo, areaOfEffect);
			assertTrue(found.size() > 0);
			assertTrue(found.containsAll(desired));
			assertTrue(desired.containsAll(found));

			// beyond range
			sourcePos = new Vector(0.0, 0.0, 0.0);
			startPos = new Vector(range + 1.0, 0.0, 0.0);
			pointTo = new Vector(2.0, 0.0, 0.0);

			try {
				VirtualBoard.objectsInAreaOfEffect(sourcePos, startPos, pointTo, areaOfEffect);
				fail("This line of code should not be reached");
			} catch (OutOfRangeException ex) {
				// expected exception
				assertTrue(true);
			}

		} catch (ParseException ex) {
			ex.printStackTrace();
			fail("Parser exception");
		} catch (InvalidAreaOfEffectException ex) {
			ex.printStackTrace();
			fail("Invalid area_of_effect structure");
		} catch (OutOfRangeException ex) {
			ex.printStackTrace();
			fail("Unexpected out of range error");
		}
	}

}
