package gameobjects.entities;

import dnd.data.Alignment;
import dnd.data.Condition;
import dnd.data.CreatureType;
import dnd.data.DamageType;
import dnd.data.SizeCategory;
import dnd.tasks.Task;
import maths.Vector;

public class Zombie extends Entity {

	public Zombie(Vector pos, Vector rot) {
		super("Zombie", pos, rot);
		
		size = SizeCategory.MEDIUM;
		type = CreatureType.UNDEAD;
		alignment = Alignment.NEUTRAL_EVIL;
		baseArmorClass = 10;
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
		conditionImmunities.add(Condition.POISONED);
		immunities.add(DamageType.POISON);
		
		Task task1 = new dnd.tasks.Dodge();
		Task task2 = new dnd.tasks.Disengage();
		Task task3 = new dnd.tasks.ExtraAttack3(inventory);
		addBaseTask(task1);
		addBaseTask(task2);
		addBaseTask(task3);
		resetTasks();
		//inventory.equipWeapon(new Club());
		
	}
	
}
