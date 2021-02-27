package main;

import dnd.effects.Effect;
import gameobjects.entities.Entity;
import gameobjects.entities.Zombie;
import maths.Vector;

public class Main {

	// public static Window window;

	public static void main(String[] args) {
		test();
	}

	private static void test() {
		Entity zombie1 = new Zombie(new Vector(30, 0, 0), new Vector(1, 0, 0));
		zombie1.invokeTask(0);
		zombie1.invokeQueuedEvent(0, 0, zombie1.getPos());

		for (Effect e : zombie1.getEffects()) {
			System.out.println(e);
		}

//		zombie1.addEffect(new dnd.effects.Dodge(zombie1, zombie1));

	}

}
