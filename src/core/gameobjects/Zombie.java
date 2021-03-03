package core.gameobjects;

import core.Item;
import core.tasks.Attack;
import core.tasks.Task;
import maths.Vector;

/**
 * This class represents a Zombie game piece
 * 
 * @author calvi
 *
 */
public class Zombie extends Entity {

	/**
	 * Constructor for class Zombie
	 * 
	 * @param pos ({@code Vector}) the coordinate at which the Zombie is located
	 * @param rot ({@code Vector}) a Vector in parallel with the direction the
	 *            Zombie is facing
	 */
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
//		task = new Task("scripts/tasks/dodge.lua", "Dodge");
//		addBaseTask(task);
		task = new Attack(this, 3);
		addBaseTask(task);
		resetTasks();

		equipMainhand(new Item("scripts/items/club.lua"));
	}

}
