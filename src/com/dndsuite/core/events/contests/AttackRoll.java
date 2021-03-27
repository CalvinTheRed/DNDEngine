package com.dndsuite.core.events.contests;

import java.util.LinkedList;

import com.dndsuite.core.effects.Effect;
import com.dndsuite.core.events.Event;
import com.dndsuite.core.gameobjects.GameObject;

public class AttackRoll extends DiceContest {
	public static final String CRITICAL_HIT = "Critical Hit";
	public static final String CRITICAL_MISS = "Critical Miss";

	protected int critThresh;

	public AttackRoll(int attackAbility, Event parent, GameObject target) {
		super(null, attackAbility, parent, target);
		critThresh = 20;
		setName(AttackRoll.getEventID());
		addTag(AttackRoll.getEventID());
	}

	@Override
	public AttackRoll clone() {
		AttackRoll clone = new AttackRoll(eventAbility, parent, target);
		cloneDataTo(clone);
		clone.shortrange = shortrange;
		clone.longrange = longrange;
		clone.radius = radius;
		clone.appliedEffects = new LinkedList<Effect>();
		clone.appliedEffects.addAll(appliedEffects);
		clone.tags = new LinkedList<String>();
		clone.tags.addAll(tags);

		clone.d20 = d20.clone();
		clone.bonus = bonus;

		clone.critThresh = critThresh;
		return clone;
	}

	public int getCriticalThreshold() {
		return critThresh;
	}

	@Override
	public void roll() {
		super.roll();
		if (getRawRoll() >= critThresh) {
			System.out.println("[JAVA] CRITICAL HIT!");
			addTag(DiceContest.SET_PASS);
			addTag(CRITICAL_HIT);
		} else if (getRawRoll() == 1) {
			System.out.println("[JAVA] CRITICAL MISS!");
			addTag(DiceContest.SET_FAIL);
			addTag(CRITICAL_MISS);
		}
	}

	public void setCriticalThreshold(int critThresh) {
		this.critThresh = critThresh;
	}

	public static String getEventID() {
		return "Attack Roll";
	}

}
