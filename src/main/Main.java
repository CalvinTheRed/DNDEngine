package main;

import core.events.contests.SavingThrow;
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
		group.addEvent(new SavingThrow("scripts/events/contests/sacred_flame.lua", Entity.DEX));

		z.queueEventGroup(group);
		z.invokeQueuedEvent(0, 0, z.getPos());
	}

}
