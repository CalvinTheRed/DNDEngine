package com.dndsuite.core.events;

import com.dndsuite.core.gameobjects.Entity;
import com.dndsuite.maths.Vector;

public class DiceCheckCalculation extends Event {
	private static final int BASE_DC = 8;

	protected Entity parent;
	protected int dcAbility;
	protected int bonus;

	public DiceCheckCalculation(Entity parent, int dcAbility) {
		super(null);
		this.parent = parent;
		this.dcAbility = dcAbility;
		bonus = 0;
		setName(DiceCheckCalculation.getEventID());
		addTag(DiceCheckCalculation.getEventID());
	}

	public void addBonus(int bonus) {
		this.bonus += bonus;
	}

	public int getBonus() {
		return bonus;
	}

	public int getDC() {
		return BASE_DC + parent.getAbilityModifier(dcAbility) + bonus;
	}

	public Entity getParent() {
		return parent;
	}

	public static String getEventID() {
		return "Dice Check Calculation";
	}

	@Override
	public void invoke(Entity source, Vector targetPos) {
	}

}
