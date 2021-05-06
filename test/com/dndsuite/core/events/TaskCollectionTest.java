package com.dndsuite.core.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.LinkedList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.dndsuite.core.effects.Effect;
import com.dndsuite.core.gameobjects.Entity;
import com.dndsuite.core.tasks.Task;
import com.dndsuite.maths.Vector;

class TaskCollectionTest {
	private TaskCollection tc;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		tc = new TaskCollection();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	@DisplayName("Sanity check")
	void test1() {
		assertEquals(TaskCollection.getEventID(), tc.toString());
		assertEquals(0, tc.getEffects().size());
		assertEquals(0, tc.getTasks().size());
		LinkedList<String> desiredTags = new LinkedList<String>();
		desiredTags.add(TaskCollection.getEventID());
		assertIterableEquals(desiredTags, tc.getTags());
	}
	
	@Test
	@DisplayName("Adding and removing Tasks")
	void test2() {
		// Single task
		Task task1 = new Task("testing/tasks/dummy.lua");
		tc.addTask(task1);
		assertEquals(1, tc.getTasks().size());
		assertEquals(task1, tc.getTasks().get(0));
		
		// Removing Task
		tc.removeTask(task1);
		assertEquals(0, tc.getTasks().size());
		
		// Collection of tasks
		LinkedList<Task> tasks = new LinkedList<Task>();
		Task task2 = new Task("testing/tasks/dummy.lua");
		Task task3 = new Task("testing/tasks/dummy.lua");
		tasks.add(task1);
		tasks.add(task2);
		tasks.add(task3);
		tc.addTasks(tasks);
		assertIterableEquals(tasks, tc.getTasks());
	}
	
	@Test
	@DisplayName("TaskCollection cloning")
	void test3() {
		Entity tmp;
		// populate tc with data to be cloned
		tmp = new Entity("testing/entities/dummy.lua", new Vector(), new Vector());
		tc.addTag("TEST TAG 1");
		tc.addTarget(tmp);
		tc.addTask(new Task("testing/tasks/dummy.lua"));
		try {
			tc.applyEffect(new Effect("testing/effects/dummy.lua", tmp, tmp));
		} catch (Exception ex) {
			fail("FAIL - Dummy Effect failed to apply to fresh TaskCollection Event");
		}
		
		TaskCollection clone = tc.clone();
		
		// Verify data was successfully cloned
		assertIterableEquals(tc.getTags(), clone.getTags());
		assertIterableEquals(tc.getTargets(), clone.getTargets());
		assertIterableEquals(tc.getTasks(), clone.getTasks());
		assertIterableEquals(tc.getEffects(), clone.getEffects());
		
		// Modify clone data
		tmp = new Entity("testing/entities/dummy.lua", new Vector(), new Vector());
		clone.addTag("TEST TAG 2");
		clone.addTarget(tmp);
		clone.addTask(new Task("testing/tasks/dummy.lua"));
		try {
			tc.applyEffect(new Effect("testing/effects/dummy.lua", tmp, tmp));
		} catch (Exception ex) {
			fail("FAIL - Dummy Effect failed to apply to fresh TaskCollection Event");
		}
		
		// Verify modifying clone data does not influence tc
		assertNotEquals(tc.getTags().size(), clone.getTags().size());
		assertNotEquals(tc.getTargets().size(), clone.getTargets().size());
		assertNotEquals(tc.getTasks().size(), clone.getTasks().size());
		assertNotEquals(tc.getEffects().size(), clone.getEffects().size());
	}

}
