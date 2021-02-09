package dnd.events.dicecontest;

import dnd.items.Item;
import engine.Manager;
import gameobjects.entities.Entity;
import maths.Vector;

public class RangedWeaponAttack extends AttackRoll {
	Item weapon;
	
	public RangedWeaponAttack(Item weapon) {
		super("Ranged Attack (" + weapon.toString() + ")");
		this.weapon = weapon;
	}
	
	@Override
	public void invoke(Entity source, Vector targetPos) {
		Entity target = Manager.entityAt(targetPos);
		System.out.println(source + " makes a ranged weapon attack using " + (weapon == null ? "Fist" : weapon) + " against " + target);
		
		while (source.processEvent(this, target) || target.processEvent(this, target));
		
		roll();
		
		while (source.processEvent(this, target) || target.processEvent(this, target));
	}
	
}
