package main;

import gameobjects.entities.Entity;
import gameobjects.entities.Zombie;
import maths.Vector;

public class Main {

	// public static Window window;

	public static void main(String[] args) {
		// initialize();
		test();
	}

	private static void initialize() {
		// Window.setDefaultLookAndFeelDecorated(false);
		// window = new Window();
	}

	private static void test() {
		Entity zombie1 = new Zombie(new Vector(30, 0, 0), new Vector(1, 0, 0));
		zombie1.invokeTask(0);
		zombie1.invokeQueuedEvent(0, 0, new Vector(0, 0, 0));

//		zombie1.addEffect(new dnd.effects.Dodge(zombie1, zombie1));

	}

}
