package com.dndsuite.dnd;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
		// e = new Entity("testing/entities/dummy.lua",new Vector(0, 0, 0), new
		// Vector(1, 0, 0));
	}

	@AfterEach
	void tearDown() throws Exception {
		VirtualBoard.clearGameObjects();
	}

	@Test
	@DisplayName("GameObjects can be added to the VirtualBoard")
	void test1() {
//		VirtualBoard.addGameObject(e);
//		assertTrue(VirtualBoard.contains(e));
	}

	@Test
	@DisplayName("Particular GameObjects can be removed from the VirtualBoard")
	void test2() {
//		VirtualBoard.addGameObject(e);
//		VirtualBoard.removeGameObject(e);
//		assertFalse(VirtualBoard.contains(e));
	}

	@Test
	@DisplayName("VirtualBoard can have its GameObject list cleared")
	void test3() {
//		VirtualBoard.addGameObject(e);
//		assertTrue(VirtualBoard.contains(e));
//		VirtualBoard.clearGameObjects();
//		assertFalse(VirtualBoard.contains(e));
	}

	@Test
	@DisplayName("VirtualBoard can locate the nearest GameObject")
	void test4() {
//		e = new Entity("testing/entities/dummy.lua", new Vector(0, 0, 0), new Vector(1, 0, 0));
//		VirtualBoard.addGameObject(e);
//		e = new Entity("testing/entities/dummy.lua", new Vector(1, 0, 0), new Vector(1, 0, 0));
//		VirtualBoard.addGameObject(e);
//		e = new Entity("testing/entities/dummy.lua", new Vector(2, 0, 0), new Vector(1, 0, 0));
//		VirtualBoard.addGameObject(e);
//
//		assertEquals(VirtualBoard.nearestObject(new Vector(2, 0, 0), new String[] {}), e);
//		assertEquals(VirtualBoard.nearestObject(new Vector(2.1, 0, 0), new String[] {}), e);
	}

	@Test
	@DisplayName("VirtualBoard can locate GameObjects within a particular cone")
	void test5() {
//		double length = 10.0;
//		LinkedList<GameObject> fullList = new LinkedList<GameObject>();
//
//		for (int i = -(int) length - 1; i <= (int) length + 1; i++) {
//			for (int k = -(int) length - 1; k <= (int) length + 1; k++) {
//				e = new Entity("testing/entities/dummy.lua", new Vector(i, 0, k), new Vector(1, 0, 0));
//				fullList.add(e);
//				VirtualBoard.addGameObject(e);
//			}
//		}
//
//		// An Entity located at the source should not be returned by entitiesInCone()
//		LinkedList<GameObject> list = VirtualBoard.objectsInCone(new Vector(0, 0, 0), new Vector(1, 0, 0), length,
//				new String[] {});
//
//		for (GameObject e : fullList) {
//			if (e.getPos().x() >= 0 && e.getPos().x() <= length
//					&& Math.abs(e.getPos().z()) <= Math.sin(Math.toRadians(22.5)) * e.getPos().x()
//					&& !e.getPos().equalTo(new Vector(0, 0, 0))) {
//				assertTrue(list.contains(e));
//			} else {
//				assertFalse(list.contains(e));
//			}
//		}
	}

	@Test
	@DisplayName("VirtualBoard can locate GameObjects within a particular cube")
	void test6() {
//		double radius = 10.0;
//		LinkedList<GameObject> fullList = new LinkedList<GameObject>();
//
//		for (int i = -(int) radius - 1; i <= (int) radius + 1; i++) {
//			for (int k = -(int) radius - 1; k <= (int) radius + 1; k++) {
//				e = new Entity("testing/entities/dummy.lua", new Vector(i, 0, k), new Vector(1, 0, 0));
//				fullList.add(e);
//				VirtualBoard.addGameObject(e);
//			}
//		}
//
//		LinkedList<GameObject> list = VirtualBoard.objectsInCube(new Vector(0, 0, 0), new Vector(1, 0, 0), radius,
//				new String[] {});
//
//		for (GameObject e : fullList) {
//			if (Math.abs(e.getPos().x()) <= radius && Math.abs(e.getPos().z()) <= radius) {
//				assertTrue(list.contains(e));
//			} else {
//				assertFalse(list.contains(e));
//			}
//		}
	}

	@Test
	@DisplayName("VirtualBoard can locate GameObjects within a particular line (cylinder)")
	void test7() {
//		double length = 10.0;
//		double radius = 5.0;
//		LinkedList<GameObject> fullList = new LinkedList<GameObject>();
//
//		for (int i = -(int) length - 1; i <= (int) length + 1; i++) {
//			for (int k = -(int) length - 1; k <= (int) length + 1; k++) {
//				e = new Entity("testing/entities/dummy.lua", new Vector(i, 0, k), new Vector(1, 0, 0));
//				fullList.add(e);
//				VirtualBoard.addGameObject(e);
//			}
//		}
//
//		// An Entity("scripts/test/dummy.lua", located at the source should not be returned by entitiesInLine()
//		LinkedList<GameObject> list = VirtualBoard.objectsInLine(new Vector(0, 0, 0), new Vector(1, 0, 0), length,
//				radius, new String[] {});
//
//		for (GameObject e : fullList) {
//			if (e.getPos().x() >= 0 && e.getPos().x() <= length && Math.abs(e.getPos().z()) <= radius
//					&& !e.getPos().equalTo(new Vector(0, 0, 0))) {
//				assertTrue(list.contains(e));
//			} else {
//				assertFalse(list.contains(e));
//			}
//		}
	}

	@Test
	@DisplayName("VirtualBoard can locate GameObjects within a particular sphere")
	void test8() {
//		double radius = 10.0;
//		LinkedList<GameObject> fullList = new LinkedList<GameObject>();
//
//		for (int i = -(int) radius - 1; i <= (int) radius + 1; i++) {
//			for (int k = -(int) radius - 1; k <= (int) radius + 1; k++) {
//				e = new Entity("testing/entities/dummy.lua", new Vector(i, 0, k), new Vector(1, 0, 0));
//				fullList.add(e);
//				VirtualBoard.addGameObject(e);
//			}
//		}
//
//		LinkedList<GameObject> list = VirtualBoard.objectsInSphere(new Vector(0, 0, 0), radius, new String[] {});
//
//		for (GameObject e : fullList) {
//			if (e.getPos().mag() <= radius) {
//				assertTrue(list.contains(e));
//			} else {
//				assertFalse(list.contains(e));
//			}
//		}
	}

}
