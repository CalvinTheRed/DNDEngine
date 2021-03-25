package com.dndsuite.core.tasks;

import com.dndsuite.core.events.groups.EventGroup;
import com.dndsuite.core.events.groups.WeaponAttackGroup;
import com.dndsuite.core.gameobjects.Entity;

public class Attack extends Task {
	protected int numAttacks;

	public Attack(Entity subject, int numAttacks) {
		super(null);
		for (int i = 0; i < numAttacks; i++) {
			WeaponAttackGroup group = new WeaponAttackGroup();
			subject.addObserver(group);
			addEventGroup(group);
		}
		subject.updateObservers();
		this.numAttacks = numAttacks;
		name = "Attack";
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
