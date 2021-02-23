package testing.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import engine.VirtualBoard;
import gameobjects.entities.Entity;
import maths.Vector;
import testing.gameobjects.entities.DummyEntity;

public class VirtualBoardTester {

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
	@DisplayName("GameObjects can be added to the VirtualBoard")
	void test1() {
		Entity e = new DummyEntity("Dummy Entity", new Vector(0, 0, 0), new Vector(1, 0, 0));
		VirtualBoard.addGameObject(e);
		assertTrue(VirtualBoard.containsGameObject(e));
	}

	@Test
	@DisplayName("Particular GameObjects can be removed from the VirtualBoard")
	void test2() {
		Entity e = new DummyEntity("Dummy Entity", new Vector(0, 0, 0), new Vector(1, 0, 0));
		VirtualBoard.addGameObject(e);
		VirtualBoard.removeGameObject(e);
		assertFalse(VirtualBoard.containsGameObject(e));
	}

	@Test
	@DisplayName("VirtualBoard can have its GameObject list cleared")
	void test3() {
		Entity e = new DummyEntity("Dummy Entity", new Vector(0, 0, 0), new Vector(1, 0, 0));
		VirtualBoard.addGameObject(e);
		VirtualBoard.clearGameObjects();
		assertFalse(VirtualBoard.containsGameObject(e));
	}

	@Test
	@DisplayName("VirtualBoard can locate Entities at particular locations")
	void test4() {
		Vector pos = new Vector(0, 0, 0);
		Entity e = new DummyEntity("Dummy Entity", pos, new Vector(1, 0, 0));
		VirtualBoard.addGameObject(e);
		assertEquals(VirtualBoard.entityAt(pos), e);
		assertEquals(VirtualBoard.entityAt(pos.add(new Vector(1, 1, 1))), null);
	}

	@Test
	@DisplayName("VirtualBoard can locate Entities within a particular cone")
	void test5() {
		double length = 10.0;
		LinkedList<Entity> fullList = new LinkedList<Entity>();

		for (int i = -(int) length - 1; i <= (int) length + 1; i++) {
			for (int k = -(int) length - 1; k <= (int) length + 1; k++) {
				Entity e = new DummyEntity("Dummy Entity", new Vector(i, 0, k), new Vector(1, 0, 0));
				fullList.add(e);
				VirtualBoard.addGameObject(e);
			}
		}

		// An entity located at the source should not be returned by entitiesInCone()
		LinkedList<Entity> list = VirtualBoard.entitiesInCone2(new Vector(0, 0, 0), new Vector(1, 0, 0), length);

		for (Entity e : fullList) {
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
	@DisplayName("VirtualBoard can locate Entities within a particular cube")
	void test6() {
		double radius = 10.0;
		LinkedList<Entity> fullList = new LinkedList<Entity>();

		for (int i = -(int) radius - 1; i <= (int) radius + 1; i++) {
			for (int k = -(int) radius - 1; k <= (int) radius + 1; k++) {
				Entity e = new DummyEntity("Dummy Entity", new Vector(i, 0, k), new Vector(1, 0, 0));
				fullList.add(e);
				VirtualBoard.addGameObject(e);
			}
		}

		LinkedList<Entity> list = VirtualBoard.entitiesInCube(new Vector(0, 0, 0), new Vector(1, 0, 0), radius);

		for (Entity e : fullList) {
			if (Math.abs(e.getPos().x()) <= radius && Math.abs(e.getPos().z()) <= radius) {
				assertTrue(list.contains(e));
			} else {
				assertFalse(list.contains(e));
			}
		}
	}

	@Test
	@DisplayName("VirtualBoard can locate Entities within a particular line (cylinder)")
	void test7() {
		double length = 10.0;
		double radius = 5.0;
		LinkedList<Entity> fullList = new LinkedList<Entity>();

		for (int i = -(int) length - 1; i <= (int) length + 1; i++) {
			for (int k = -(int) length - 1; k <= (int) length + 1; k++) {
				Entity e = new DummyEntity("Dummy Entity", new Vector(i, 0, k), new Vector(1, 0, 0));
				fullList.add(e);
				VirtualBoard.addGameObject(e);
			}
		}

		// An entity located at the source should not be returned by entitiesInLine()
		LinkedList<Entity> list = VirtualBoard.entitiesInLine(new Vector(0, 0, 0), new Vector(1, 0, 0), length, radius);

		for (Entity e : fullList) {
			if (e.getPos().x() >= 0 && e.getPos().x() <= length && Math.abs(e.getPos().z()) <= radius
					&& !e.getPos().equalTo(new Vector(0, 0, 0))) {
				assertTrue(list.contains(e));
			} else {
				assertFalse(list.contains(e));
			}
		}
	}

	@Test
	@DisplayName("VirtualBoard can locate Entities within a particular sphere")
	void test8() {
		double radius = 10.0;
		LinkedList<Entity> fullList = new LinkedList<Entity>();

		for (int i = -(int) radius - 1; i <= (int) radius + 1; i++) {
			for (int k = -(int) radius - 1; k <= (int) radius + 1; k++) {
				Entity e = new DummyEntity("Dummy Entity", new Vector(i, 0, k), new Vector(1, 0, 0));
				fullList.add(e);
				VirtualBoard.addGameObject(e);
			}
		}

		LinkedList<Entity> list = VirtualBoard.entitiesInSphere(new Vector(0, 0, 0), radius);

		for (Entity e : fullList) {
			if (e.getPos().mag() <= radius) {
				assertTrue(list.contains(e));
			} else {
				assertFalse(list.contains(e));
			}
		}
	}

}
