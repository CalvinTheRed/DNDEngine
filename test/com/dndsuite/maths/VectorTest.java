package com.dndsuite.maths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.dndsuite.maths.Vector;

public class VectorTest {

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
	}

	@Test
	@DisplayName("Vector constructors and getters work")
	void test1() {
		Vector v = new Vector();
		assertEquals(v.x(), 0);
		assertEquals(v.y(), 0);
		assertEquals(v.z(), 0);

		v = new Vector(1, 2, 3);
		assertEquals(v.x(), 1);
		assertEquals(v.y(), 2);
		assertEquals(v.z(), 3);
	}

	@Test
	@DisplayName("Vector addition works")
	void test2() {
		Vector v1 = new Vector(1, 2, 3);
		Vector v2 = new Vector(4, 5, 6);
		Vector v3 = v1.add(v2);
		assertEquals(v3.x(), v1.x() + v2.x());
		assertEquals(v3.y(), v1.y() + v2.y());
		assertEquals(v3.z(), v1.z() + v2.z());
	}

	@Test
	@DisplayName("Vector subtraction works")
	void test3() {
		Vector v1 = new Vector(1, 2, 3);
		Vector v2 = new Vector(4, 5, 6);
		Vector v3 = v1.sub(v2);
		assertEquals(v3.x(), v1.x() - v2.x());
		assertEquals(v3.y(), v1.y() - v2.y());
		assertEquals(v3.z(), v1.z() - v2.z());
	}

	@Test
	@DisplayName("Vector dotting works")
	void test4() {
		Vector v1 = new Vector(1, 2, 3);
		Vector v2 = new Vector(4, 5, 6);
		double dot = v1.dot(v2);
		assertEquals(dot, v1.x() * v2.x() + v1.y() * v2.y() + v1.z() * v2.z());
	}

	@Test
	@DisplayName("Vector comparison works")
	void test5() {
		Vector v1 = new Vector(1, 2, 3);
		Vector v2 = new Vector(4, 5, 6);
		assertTrue(v1.equalTo(v1));
		assertFalse(v1.equalTo(v2));
	}

	@Test
	@DisplayName("Vector crossing works")
	void test6() {
		Vector v1 = new Vector(1, 2, 3);
		Vector v2 = new Vector(4, 5, 6);
		Vector v3 = v1.cross(v2);
		assertTrue(v3.equalTo(new Vector(-3, 6, -3)));
	}

	@Test
	@DisplayName("Vector magnitude works")
	void test7() {
		Vector v = new Vector(3, 4, 0);
		assertEquals(v.mag(), 5);
	}

	@Test
	@DisplayName("Vector scaling works")
	void test8() {
		Vector v = new Vector(3, 4, 1);
		assertTrue(v.scale(3).equalTo(new Vector(9, 12, 3)));
	}

	@Test
	@DisplayName("Vector unitization works")
	void test9() {
		Vector v = new Vector(2, 0, 0);
		assertTrue(v.unit().equalTo(new Vector(1, 0, 0)));
	}

	@Test
	@DisplayName("Vector projection works")
	void test10() {
		Vector v1 = new Vector(1, 1, 1);
		Vector v2 = new Vector(4, 0, 0);
		assertTrue(v1.proj(v2).equalTo(new Vector(1, 0, 0)));
	}

	@Test
	@DisplayName("Vector angle comparison works")
	void test11() {
		Vector v1 = new Vector(1, 0, 0);
		Vector v2 = new Vector(2, 0, 2);
		assertEquals(Math.round(v1.calculateAngleDiff(v2)) / 1.0, Math.round(Math.toRadians(45.0)) / 1.0);
	}

}
