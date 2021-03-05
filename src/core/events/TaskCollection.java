package core.events;

import java.util.LinkedList;

import core.gameobjects.Entity;
import core.tasks.Task;
import maths.Vector;

public class TaskCollection extends Event {
	public static final String EVENT_TAG_ID = "Task Collection";

	protected LinkedList<Task> tasks;

	public TaskCollection(Entity parent) {
		super(null);
		tasks = new LinkedList<Task>();
		addTag(Event.SINGLE_TARGET);
		addTag(TaskCollection.EVENT_TAG_ID);
	}

	public void addTask(Task task) {
		tasks.add(task);
	}

	public void addTasks(LinkedList<Task> tasklist) {
		tasks.addAll(tasklist);
	}

	public void removeTask(Task t) {
		tasks.remove(t);
	}

	public LinkedList<Task> getTasks() {
		return tasks;
	}

	@Override
	public void invoke(Entity source, Vector targetPos) {
	}

}
