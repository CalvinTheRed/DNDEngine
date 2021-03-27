package com.dndsuite.core.events;

import java.util.LinkedList;

import com.dndsuite.core.effects.Effect;
import com.dndsuite.core.gameobjects.Entity;
import com.dndsuite.core.gameobjects.GameObject;

public class DiceCheckCalculation extends Event {
	private static final int DEFAULT_BASE_DC = 8;

	protected Entity subject;
	protected int bonus;

	public DiceCheckCalculation(int dcAbility, Entity subject) {
		super(null, dcAbility);
		this.subject = subject;
	}

	public void addBonus(int bonus) {
		this.bonus += bonus;
	}

	@Override
	public DiceCheckCalculation clone() {
		DiceCheckCalculation clone = new DiceCheckCalculation(eventAbility, subject);
		cloneDataTo(clone);
		clone.shortrange = shortrange;
		clone.longrange = longrange;
		clone.radius = radius;
		clone.appliedEffects = new LinkedList<Effect>();
		clone.appliedEffects.addAll(appliedEffects);
		clone.tags = new LinkedList<String>();
		clone.tags.addAll(tags);

		clone.bonus = bonus;
		return clone;
	}

	public int getBonus() {
		return bonus;
	}

	public int getDC() {
		return DEFAULT_BASE_DC + subject.getAbilityModifier(eventAbility) + bonus;
	}

	public Entity getSubject() {
		return subject;
	}

	@Override
	public void invokeEvent(Entity source, GameObject target) {
		bonus = 0;
		while (target.processEvent(this, source, target))
			;
	}

}
