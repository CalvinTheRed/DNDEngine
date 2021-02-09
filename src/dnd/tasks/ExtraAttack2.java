package dnd.tasks;

import dnd.events.eventgroups.WeaponAttackGroup;
import dnd.items.Inventory;

public class ExtraAttack2 extends Task {

	public ExtraAttack2(Inventory inventory) {
		super("Extra Attack");
		WeaponAttackGroup group1 = new WeaponAttackGroup();
		WeaponAttackGroup group2 = new WeaponAttackGroup();
		WeaponAttackGroup group3 = new WeaponAttackGroup();
		inventory.addObserver(group1);
		inventory.addObserver(group2);
		inventory.addObserver(group3);
		addEventGroup(group1);
		addEventGroup(group2);
		addEventGroup(group3);
		inventory.updateObservers();
	}

}
