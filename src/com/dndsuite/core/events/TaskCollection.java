package com.dndsuite.core.events;

import java.util.LinkedList;

import com.dndsuite.core.effects.Effect;
import com.dndsuite.core.gameobjects.Entity;
import com.dndsuite.core.tasks.Task;
import com.dndsuite.maths.Vector;

public class TaskCollection extends Event {
	protected LinkedList<Task> tasks;

	public TaskCollection() {
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
		TaskCollection clone = new TaskCollection();
		cloneDataTo(clone);
		clone.shortrange = shortrange;
		clone.longrange = longrange;
		clone.radius = radius;
		clone.appliedEffects = new LinkedList<Effect>();
		clone.appliedEffects.addAll(appliedEffects);
		
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
