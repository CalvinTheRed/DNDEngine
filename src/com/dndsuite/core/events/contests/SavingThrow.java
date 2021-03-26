package com.dndsuite.core.events.contests;

import com.dndsuite.core.events.Event;
import com.dndsuite.core.gameobjects.GameObject;

public class SavingThrow extends DiceContest {
	protected int saveAbility;
	protected GameObject target;

	public SavingThrow(int dcAbility, GameObject target, Event parent, int saveAbility) {
		super(null, dcAbility, parent, target);
		this.saveAbility = saveAbility;
		setName(SavingThrow.getEventID());
		addTag(SavingThrow.getEventID());
	}

	@Override
	public SavingThrow clone() {
		SavingThrow clone = (SavingThrow) super.clone();
		clone.saveAbility = saveAbility;
		return clone;
	}

	public int getSaveAbility() {
		return saveAbility;
	}

	public static String getEventID() {
		return "Saving Throw";
	}

}
