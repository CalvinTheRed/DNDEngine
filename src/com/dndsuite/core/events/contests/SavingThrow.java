package com.dndsuite.core.events.contests;

import java.util.LinkedList;

import com.dndsuite.core.effects.Effect;
import com.dndsuite.core.events.Event;
import com.dndsuite.core.gameobjects.GameObject;

public class SavingThrow extends DiceContest {
	protected int saveAbility;
	protected GameObject target;

	public SavingThrow(int saveAbility, Event parent, GameObject target) {
		super(null, saveAbility, parent, target);
		this.saveAbility = saveAbility;
		setName(SavingThrow.getEventID());
		addTag(SavingThrow.getEventID());
	}

	@Override
	public SavingThrow clone() {
		SavingThrow clone = new SavingThrow(saveAbility, parent, target);
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
		return clone;
	}

	public int getSaveAbility() {
		return saveAbility;
	}

	public static String getEventID() {
		return "Saving Throw";
	}

}
