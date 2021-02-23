package dnd.tasks;

import dnd.events.eventgroups.WeaponAttackGroup;
import dnd.items.Inventory;

/**
 * A class which represents the ability of an Entity to make a physical attack
 * roll of some kind. This may be a weapon attack or a natural attack (such as
 * biting)
 * 
 * @author calvi
 *
 */
public class Attack extends Task {
	protected int numAttacks;

	/**
	 * Constructor for class Attack
	 * 
	 * @param inventory  ({@code Inventory})
	 * @param numAttacks
	 */
	public Attack(Inventory inventory, int numAttacks) {
		super("Attack");
		for (int i = 0; i < numAttacks; i++) {
			WeaponAttackGroup group = new WeaponAttackGroup();
			inventory.addObserver(group);
			addEventGroup(group);
		}
		inventory.updateObservers(); // TODO: make compatible with natural weapon options? Perhaps after merging
										// Inventory with Entity
		this.numAttacks = numAttacks;
	}

	/**
	 * This function returns the number of attacks which can be generated using this
	 * Task
	 * 
	 * @return {@code int} number of attacks
	 */
	public int getNumAttacks() {
		return numAttacks;
	}

}
