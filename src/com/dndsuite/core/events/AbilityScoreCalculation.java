package com.dndsuite.core.events;

import java.util.LinkedList;

import com.dndsuite.core.effects.Effect;
import com.dndsuite.core.gameobjects.Entity;

public class AbilityScoreCalculation extends Event {
	protected Entity subject;
	protected int base;
	protected int bonus;
	protected int setHigh;
	protected int setLow;
	
	public AbilityScoreCalculation(int abilityScore, Entity subject) {
		super(null, abilityScore);
		this.subject = subject;
		base = subject.getBaseAbilityScore(abilityScore);
		bonus = 0;
		setHigh = -1;
		setLow = -1;
	}
	
	public void addBonus(int bonus) {
		this.bonus += bonus;
	}
	
	@Override
	public AbilityScoreCalculation clone() {
		AbilityScoreCalculation clone = new AbilityScoreCalculation(eventAbility, subject);
		cloneDataTo(clone);
		clone.shortrange = shortrange;
		clone.longrange = longrange;
		clone.radius = radius;
		clone.appliedEffects = new LinkedList<Effect>();
		clone.appliedEffects.addAll(appliedEffects);
		clone.tags = new LinkedList<String>();
		clone.tags.addAll(tags);

		clone.base = base;
		clone.bonus = bonus;
		clone.setHigh = setHigh;
		clone.setLow = setLow;
		return clone;
	}
	
	public int getScore() {
		int score = base + bonus;
		if (setHigh >= 0 ) {
			if (setHigh > score) {
				return setHigh;
			}
			return score;
		} else if (setLow >= 0) {
			if (setLow < score) {
				return setLow;
			}
		}
		return score;
	}
	
	public void raiseTo(int target) {
		if (target > setHigh) {
			setHigh = target;
		}
	}
	
	public void lowerTo(int target) {
		if (setLow == -1 || target < setLow) {
			setLow = target;
		}
	}

}
