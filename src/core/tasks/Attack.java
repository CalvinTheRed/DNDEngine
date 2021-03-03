package core.tasks;

import core.events.groups.EventGroup;
import core.events.groups.WeaponAttackGroup;
import core.gameobjects.Entity;

public class Attack extends Task {
	protected int numAttacks;

	public Attack(Entity subject, int numAttacks) {
		super(null, "Attack");
		for (int i = 0; i < numAttacks; i++) {
			WeaponAttackGroup group = new WeaponAttackGroup();
			subject.addObserver(group);
			addEventGroup(group);
		}
		subject.updateObservers(); // TODO: make compatible with natural weapon options? Perhaps after merging
									// Inventory with Entity
		this.numAttacks = numAttacks;
	}

	public int getNumAttacks() {
		return numAttacks;
	}

	public boolean invoke(Entity invoker) {
		System.out.print("[JAVA] " + invoker + " invokes Task " + this + " (cost: ");

		String cost = "Action";

		System.out.println(cost + ")");

		// TODO: return false if insufficient action economy
		// TODO: expend action economy
		for (EventGroup group : eventGroups) {
			invoker.queueEventGroup(group);
		}
		return true;
	}

	public void setNumAttacks(int numAttacks) {
		this.numAttacks = numAttacks;
	}

}
