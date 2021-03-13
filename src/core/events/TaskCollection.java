package core.events;

import java.util.LinkedList;

import core.gameobjects.Entity;
import core.tasks.Task;
import maths.Vector;

public class TaskCollection extends Event {
	protected LinkedList<Task> tasks;

	public TaskCollection(Entity parent) {
		super(null);
		tasks = new LinkedList<Task>();
		addTag(Event.SINGLE_TARGET);
		addTag(TaskCollection.getEventID());
	}

	public void addTask(Task task) {
		tasks.add(task);
	}

	public void addTasks(LinkedList<Task> tasklist) {
		tasks.addAll(tasklist);
	}

	public void removeTask(Task task) {
		tasks.remove(task);
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

}
