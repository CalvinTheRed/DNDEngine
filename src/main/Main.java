package main;

import core.effects.Effect;
import core.events.contests.AttackRoll;
import core.events.groups.EventGroup;
import core.gameobjects.Entity;
import core.gameobjects.Zombie;
import maths.Vector;

public class Main {

	public static void main(String[] args) {
		test();
		// TODO: investigate why static final EVENT_TAG_ID did not work
		// TODO: look into static method inheritance w/ abstract classes
	}

	private static void test() {
		Zombie aggressor = new Zombie(new Vector(1, 0, 0), new Vector(0, 0, 0));
		Zombie target = new Zombie(new Vector(0, 0, 0), new Vector(0, 0, 0));

		System.out.println();

		target.addEffect(new Effect("scripts/effects/dodge.lua", target, target));

		System.out.println();

		EventGroup group = new EventGroup();
		group.addEvent(new AttackRoll("scripts/events/contests/attackrolls/ice_knife.lua", Entity.INT));

		aggressor.queueEventGroup(group);
		aggressor.invokeQueuedEvent(0, 0, target.getPos());
	}

}
