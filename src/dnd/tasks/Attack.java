package dnd.tasks;

import dnd.events.eventgroups.WeaponAttackGroup;
import dnd.items.Inventory;

public class Attack extends Task {

	public Attack(Inventory inventory) {
		super("Attack");
		WeaponAttackGroup group1 = new WeaponAttackGroup();
		inventory.addObserver(group1);
		addEventGroup(group1);
		inventory.updateObservers();
	}

}
