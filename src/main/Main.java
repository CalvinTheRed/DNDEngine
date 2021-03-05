package main;

import core.events.SpellAttack;
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

		EventGroup eg = new EventGroup();
		eg.addEvent(new SpellAttack("scripts/events/fire_bolt.lua", Entity.INT));
		z.queueEventGroup(eg);

		for (EventGroup group : z.getEventQueue()) {
			System.out.println(group);
		}

		z.invokeQueuedEvent(0, 0, z.getPos());

	}

}
