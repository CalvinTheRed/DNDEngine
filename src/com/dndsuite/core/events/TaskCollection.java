package com.dndsuite.core.events;

import java.util.LinkedList;

import com.dndsuite.core.gameobjects.Entity;
import com.dndsuite.core.tasks.Task;
import com.dndsuite.maths.Vector;

public class TaskCollection extends Event {
	protected LinkedList<Task> tasks;

	public TaskCollection(Entity parent) {
		super(null, -1);
		tasks = new LinkedList<Task>();
		setName(TaskCollection.getEventID());
		addTag(TaskCollection.getEventID());
	}

	public void addTask(Task task) {
		tasks.add(task);
	}

	public void addTasks(LinkedList<Task> tasklist) {
		tasks.addAll(tasklist);
	}

	@Override
	public TaskCollection clone() {
		TaskCollection clone = (TaskCollection) super.clone();
		clone.tasks = new LinkedList<Task>();
		clone.tasks.addAll(tasks);
		return clone;
	}

	public LinkedList<Task> getTasks() {
		return tasks;
	}

	public static String getEventID() {
		return "Task Collection";
	}

	@Override
	public void invoke(Entity source, Vector targetPos) {
	}

	public void removeTask(Task task) {
		tasks.remove(task);
	}

}
