package dnd.events.dicecontest;

import dnd.items.Item;
import engine.Manager;
import gameobjects.entities.Entity;
import maths.Vector;

public class ThrownWeaponAttack extends AttackRoll {
	Item weapon;
	
	public ThrownWeaponAttack(Item weapon) {
		super("Thrown Attack (" + weapon.toString() + ")");
		this.weapon = weapon;
	}
	
	@Override
	public void invoke(Entity source, Vector targetPos) {
		Entity target = Manager.entityAt(targetPos);
		System.out.println(source + " makes a thrown weapon attack using " + (weapon == null ? "Fist" : weapon) + " against " + target);
		
		while (source.processEvent(this, target) || target.processEvent(this, target));
		
		roll();
		
		while (source.processEvent(this, target) || target.processEvent(this, target));
	}
	
}
