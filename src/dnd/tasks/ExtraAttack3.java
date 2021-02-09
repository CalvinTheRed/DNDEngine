package dnd.tasks;

import dnd.events.eventgroups.WeaponAttackGroup;
import dnd.items.Inventory;

public class ExtraAttack3 extends Task {

	public ExtraAttack3(Inventory inventory) {
		super("Extra Attack");
		WeaponAttackGroup group1 = new WeaponAttackGroup();
		WeaponAttackGroup group2 = new WeaponAttackGroup();
		WeaponAttackGroup group3 = new WeaponAttackGroup();
		WeaponAttackGroup group4 = new WeaponAttackGroup();
		inventory.addObserver(group1);
		inventory.addObserver(group2);
		inventory.addObserver(group3);
		inventory.addObserver(group4);
		addEventGroup(group1);
		addEventGroup(group2);
		addEventGroup(group3);
		addEventGroup(group4);
		inventory.updateObservers();
	}

}
