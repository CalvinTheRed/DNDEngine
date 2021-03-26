package com.dndsuite.core.events;

import java.util.LinkedList;

import com.dndsuite.core.events.contests.AttackRoll;
import com.dndsuite.core.gameobjects.Entity;
import com.dndsuite.core.gameobjects.GameObject;
import com.dndsuite.dnd.combat.DamageDiceGroup;
import com.dndsuite.maths.Vector;
import com.dndsuite.maths.dice.Die;

public class DamageCalculation extends Event {
	protected LinkedList<DamageDiceGroup> damageDice;
	protected Event parent;

	public DamageCalculation(Event parent) {
		super(null, -1);
		this.parent = parent;
		damageDice = new LinkedList<DamageDiceGroup>();
		setName(DamageCalculation.getEventID() + " (" + parent + ")");
		addTag(DamageCalculation.getEventID());
	}

	@Override
	public DamageCalculation clone() {
		DamageCalculation clone = (DamageCalculation) super.clone();
		clone.damageDice = new LinkedList<DamageDiceGroup>();
		clone.damageDice.addAll(damageDice);
		clone.parent = parent;
		return clone;
	}

	@Override
	public void invoke(Entity source, Vector targetPos) {
		roll();
		super.invoke(source, targetPos);
	}

	@Override
	public void invokeEvent(Entity source, GameObject target) {
		Damage d = new Damage(this);
		d.invoke(source, target.getPos());
	}

	public void addDamageDiceGroup(DamageDiceGroup newGroup) {
		for (DamageDiceGroup group : damageDice) {
			if (group.getDamageType() == newGroup.getDamageType()) {
				for (Die d : newGroup.getDice()) {
					group.addDie(d);
					group.addBonus(newGroup.getBonus());
					return;
				}
			}
		}
		damageDice.add(newGroup);
	}

	public void roll() {
		if (hasTag(AttackRoll.CRITICAL_HIT)) {
			System.out.println("[JAVA] CRITICAL HIT!!!");
			LinkedList<DamageDiceGroup> criticalDice = new LinkedList<DamageDiceGroup>();
			for (DamageDiceGroup group : damageDice) {
				DamageDiceGroup criticalClone = group.clone();
				criticalClone.addBonus(-criticalClone.getBonus());
				criticalDice.add(criticalClone);
			}
			for (DamageDiceGroup group : criticalDice) {
				addDamageDiceGroup(group);
			}
		}

		for (DamageDiceGroup group : damageDice) {
			group.roll();
		}
	}

	public LinkedList<DamageDiceGroup> getDamageDice() {
		return damageDice;
	}

	public Event getParent() {
		return parent;
	}

	public static String getEventID() {
		return "Damage Calculation";
	}

}
