package com.dndsuite.core.tasks;

import com.dndsuite.core.events.groups.ItemAttackGroup;
import com.dndsuite.core.gameobjects.Entity;

public class Attack extends Task {
	protected Entity subject;
	protected int numAttacks;

	public Attack(Entity subject) {
		super(null);
		this.subject = subject;
		addAttack();
		subject.updateObservers();
		setName(Attack.getTaskID());
		addTag(Attack.getTaskID());
	}
	
	public void addAttack() {
		ItemAttackGroup group = new ItemAttackGroup(ItemAttackGroup.MAINHAND);
		subject.addObserver(group);
		addEventGroup(group);
		numAttacks++;
	}
	
	public String getCost() {
		return "Action";
	}

	public int getNumAttacks() {
		return numAttacks;
	}

	public static String getTaskID() {
		return "Attack";
	}

}
