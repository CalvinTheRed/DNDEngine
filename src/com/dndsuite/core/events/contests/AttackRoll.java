package com.dndsuite.core.events.contests;

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
		AttackRoll clone = (AttackRoll) super.clone();
		clone.critThresh = critThresh;
		return clone;
	}

	public int getCriticalThreshold() {
		return critThresh;
	}

	@Override
	protected void roll() {
		super.roll();
		if (getRawRoll() >= critThresh) {
			addTag(CRITICAL_HIT);
		}
	}

	public void setCriticalThreshold(int critThresh) {
		this.critThresh = critThresh;
	}

	public static String getEventID() {
		return "Attack Roll";
	}

}
