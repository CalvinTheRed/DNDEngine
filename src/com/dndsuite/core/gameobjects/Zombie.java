package com.dndsuite.core.gameobjects;

import com.dndsuite.core.tasks.Attack;
import com.dndsuite.core.tasks.Task;
import com.dndsuite.maths.Vector;

/**
 * This class represents a Zombie game piece
 * 
 * @author calvi
 *
 */
public class Zombie extends Entity {

	public Zombie(Vector pos, Vector rot) {
		super("Zombie", pos, rot);

		experience = 50;
		level = 3;
		abilityScores[Entity.STR] = 13;
		abilityScores[Entity.DEX] = 6;
		abilityScores[Entity.CON] = 16;
		abilityScores[Entity.INT] = 3;
		abilityScores[Entity.WIS] = 6;
		abilityScores[Entity.CHA] = 5;
		baseAbilityScores[Entity.STR] = 13;
		baseAbilityScores[Entity.DEX] = 6;
		baseAbilityScores[Entity.CON] = 16;
		baseAbilityScores[Entity.INT] = 3;
		baseAbilityScores[Entity.WIS] = 6;
		baseAbilityScores[Entity.CHA] = 5;
		generateHealth(8);

		Task task;
		task = new Attack(this, 3);
		addBaseTask(task);

		getTasks(); // populate initial availableTasks list
	}

}
