package dnd.events.dicecontest;

import dnd.items.Item;
import engine.Manager;
import gameobjects.entities.Entity;
import maths.Vector;

public class MeleeWeaponAttack extends AttackRoll {
	Item weapon;
	
	public MeleeWeaponAttack(Item weapon) {
		super("Melee Attack (" + (weapon == null ? "Fist)" : weapon.toString() + ")"));
		this.weapon = weapon;
	}
	
	@Override
	public void invoke(Entity source, Vector targetPos) {
		Entity target = Manager.entityAt(targetPos);
		System.out.println(source + " makes a melee weapon attack using " + (weapon == null ? "Fist" : weapon) + " against " + target);
		
		while (source.processEvent(this, target) || target.processEvent(this, target));
		
		roll();
		
		while (source.processEvent(this, target) || target.processEvent(this, target));
	}
	
}
