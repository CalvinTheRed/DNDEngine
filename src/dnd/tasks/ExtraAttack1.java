package dnd.tasks;

import dnd.events.eventgroups.WeaponAttackGroup;
import dnd.items.Inventory;

public class ExtraAttack1 extends Task {

	public ExtraAttack1(Inventory inventory) {
		super("Extra Attack");
		WeaponAttackGroup group1 = new WeaponAttackGroup();
		WeaponAttackGroup group2 = new WeaponAttackGroup();
		inventory.addObserver(group1);
		inventory.addObserver(group2);
		addEventGroup(group1);
		addEventGroup(group2);
		inventory.updateObservers();
	}

}
