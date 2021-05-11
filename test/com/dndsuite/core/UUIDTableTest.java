package com.dndsuite.core;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UUIDTableTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		UUIDTable.clear();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	@DisplayName("Adding to UUIDTable")
	void test001() {
		fail("Not yet implemented");
	}

}
