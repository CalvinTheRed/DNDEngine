package dnd.tasks;

import dnd.events.eventgroups.WeaponAttackGroup;
import dnd.items.Inventory;

public class Attack extends Task {
	protected int numAttacks;
	
	public Attack(Inventory inventory) {
		super("Attack");
		WeaponAttackGroup group = new WeaponAttackGroup();
		inventory.addObserver(group);
		addEventGroup(group);
		inventory.updateObservers();
		numAttacks = 1;
	}
	
	public Attack(Inventory inventory, int numAttacks) {
		super("Attack");
		for (int i = 0; i < numAttacks; i++) {
			WeaponAttackGroup group = new WeaponAttackGroup();
			inventory.addObserver(group);
			addEventGroup(group);
		}
		inventory.updateObservers();
		this.numAttacks = numAttacks;
	}
	
	public int getNumAttacks() {
		return numAttacks;
	}

}
