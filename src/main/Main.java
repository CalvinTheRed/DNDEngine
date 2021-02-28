package main;

import gameobjects.entities.Entity;
import gameobjects.entities.Zombie;
import maths.Vector;

public class Main {

	public static void main(String[] args) {
		test();
	}

	private static void test() {
		Entity z = new Zombie(new Vector(30, 0, 0), new Vector(1, 0, 0));
		z.invokeTask(0);
		z.invokeTask(0);
		z.invokeQueuedEvent(0, 0, z.getPos());
		z.invokeQueuedEvent(0, 0, z.getPos());
	}

}
