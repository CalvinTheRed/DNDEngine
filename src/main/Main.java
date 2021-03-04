package main;

import core.Item;
import core.gameobjects.Entity;
import core.gameobjects.Zombie;
import core.tasks.Task;
import maths.Vector;

public class Main {

	public static void main(String[] args) {
		test();
	}

	private static void test() {
		Entity z = new Zombie(new Vector(30, 0, 0), new Vector(1, 0, 0));
		z.equipMainhand(new Item("scripts/items/test_item.lua"));
		for (Task t : z.getTasks()) {
			System.out.println(t);
		}
	}

}
