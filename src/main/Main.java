package main;

import core.Item;
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

		Entity holder   = new Zombie(new Vector(5, 5, 0), new Vector(-1, -1, 0));
		Entity attacker = new Zombie(new Vector(4, 4, 0), new Vector( 1,  1, 0));
		
		holder.equipMainhand(new Item("scripts/items/tuna_can.lua"));
		attacker.equipMainhand(new Item("scripts/items/club.lua"));

		System.out.println("----------------------------");
		
		// Attacker's turn
		attacker.invokeTask(0);
		for (EventGroup group : attacker.getEventQueue()) {
			System.out.println(group);
		}
		attacker.invokeQueuedEvent(0, 0, holder.getPos());

		System.out.println("----------------------------");
		
		// Holder's turn
		holder.invokeTask(0);
		for (EventGroup group : holder.getEventQueue()) {
			System.out.println(group);
		}
		holder.invokeQueuedEvent(0, 1, attacker.getPos());

	}

}
