package main;

import core.Item;
import core.events.groups.EventGroup;
import core.gameobjects.Entity;
import core.gameobjects.Zombie;
import maths.Vector;

public class Main {

	public static void main(String[] args) {
		test();
	}

	private static void test() {
		Entity z = new Zombie(new Vector(30, 0, 0), new Vector(1, 0, 0));
		z.invokeTask(0);
		for (EventGroup group : z.getEventQueue()) {
			System.out.println(group);
		}
		z.invokeQueuedEvent(0, 0, z.getPos());
		for (EventGroup group : z.getEventQueue()) {
			System.out.println(group);
		}
		z.equipMainhand(new Item("scripts/items/dagger.lua"));
		for (EventGroup group : z.getEventQueue()) {
			System.out.println(group);
		}
		z.invokeQueuedEvent(0, 0, z.getPos());
		z.equipMainhand(new Item("scripts/items/dagger.lua"));
		for (EventGroup group : z.getEventQueue()) {
			System.out.println(group);
		}
	}

}
