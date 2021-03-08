package main;

import core.events.contests.AttackRoll;
import core.events.groups.EventGroup;
import core.gameobjects.Entity;
import core.gameobjects.Zombie;
import maths.Vector;

public class Main {

	public static void main(String[] args) {
		test();
	}

	private static void test() {
		Zombie z = new Zombie(new Vector(0, 0, 0), new Vector(0, 0, 0));

		EventGroup group = new EventGroup();
		group.addEvent(new AttackRoll("scripts/events/bite.lua", Entity.STR));

		z.queueEventGroup(group);
		z.invokeQueuedEvent(0, 0, z.getPos());
	}

}
