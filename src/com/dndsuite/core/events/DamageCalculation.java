package com.dndsuite.core.events;

import java.util.LinkedList;

import com.dndsuite.core.effects.Effect;
import com.dndsuite.core.events.contests.AttackRoll;
import com.dndsuite.core.gameobjects.Entity;
import com.dndsuite.core.gameobjects.GameObject;
import com.dndsuite.dnd.combat.DamageDiceGroup;
import com.dndsuite.maths.dice.Die;

public class DamageCalculation extends Event {
	public static final String CRITICAL_HIT = "Critical Hit";
	public static final String CRITICAL_MISS = "Critical Miss";
	public static final String HALVED = "Halved";

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
		DamageCalculation clone = new DamageCalculation(parent);
		cloneDataTo(clone);
		clone.shortrange = shortrange;
		clone.longrange = longrange;
		clone.radius = radius;
		clone.appliedEffects = new LinkedList<Effect>();
		clone.appliedEffects.addAll(appliedEffects);

		clone.damageDice.clear();
		clone.damageDice.addAll(damageDice);
		return clone;
	}

	@Override
	public void invokeEvent(Entity source, GameObject target) {
		while (source.processEvent(this, source, target) || target.processEvent(this, source, target))
			;
		if (hasTag(HALVED)) {
			for (DamageDiceGroup group : damageDice) {
				group.halve();
			}
		}
		target.processDamage(this);
		Damage d = new Damage(this);
		d.invokeEvent(source, target);
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

	public void roll(AttackRoll ar) {
		if (ar != null && ar.hasTag(AttackRoll.CRITICAL_HIT)) {

		}

		if (hasTag(CRITICAL_HIT)) {
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

	public String getFormula() {
		String s = "";
		for (int i = 0; i < damageDice.size(); i++) {
			DamageDiceGroup group = damageDice.get(i);
			s += group.getDice().size() + "d" + group.getDice().get(0).getSize();
			if (group.getBonus() > 0) {
				s += "+" + group.getBonus();
			}
			s += " " + group.getDamageType();
			if (i < damageDice.size() - 1) {
				s += " + ";
			}
		}
		return s;
	}

	public static String getEventID() {
		return "Damage Calculation";
	}

}
