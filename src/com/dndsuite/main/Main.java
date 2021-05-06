package com.dndsuite.main;

import com.dndsuite.core.Item;
import com.dndsuite.core.effects.ItemProficiency;
import com.dndsuite.core.gameobjects.Entity;
import com.dndsuite.maths.Vector;

public class Main {

	public static void main(String[] args) {
		Entity attacker = new Entity("scripts/entities/zombie.lua", new Vector(0, 0, 0), new Vector(1, 0, 0));
		Entity target = new Entity("scripts/entities/zombie.lua", new Vector(1, 0, 0), new Vector(1, 0, 0));
		attacker.equipMainhand(new Item("scripts/items/longsword.lua"));
		attacker.addEffect(new ItemProficiency(attacker, attacker, Item.LONGSWORD));
		attacker.getTasks();

		attacker.invokeTask(1);
		attacker.invokeQueuedEvent(0, 0, target.getPos());

	}

}
