package com.dndsuite.maths.dice;

import java.util.ArrayList;

import com.dndsuite.core.Taggable;

public class DamageDiceGroup extends DiceGroup implements Taggable {
	public static final int INEFFECTIVE = 0;
	public static final int RESISTED = 1;
	public static final int NORMAL = 2;
	public static final int ENHANCED = 3;

	protected ArrayList<String> tags;
	protected String damageType;

	public DamageDiceGroup(long numDice, long dieSize, String damageType) {
		super(numDice, dieSize);
		this.damageType = damageType;
		tags = new ArrayList<String>();
	}

	public DamageDiceGroup(long numDice, long dieSize, long bonus, String damageType) {
		super(numDice, dieSize, bonus);
		this.damageType = damageType;
		tags = new ArrayList<String>();
	}

	public String getDamageType() {
		return damageType;
	}

	public void setDamageType(String damageType) {
		this.damageType = damageType;
	}

	public int getEffectiveness() {
		int effectiveness;

		if (hasTag("immunity")) {
			effectiveness = INEFFECTIVE;
		} else if (hasTag("resistance") && hasTag("vulnerability")) {
			effectiveness = NORMAL;
		} else if (hasTag("resistance")) {
			effectiveness = RESISTED;
		} else if (hasTag("vulnerability")) {
			effectiveness = ENHANCED;
		} else {
			effectiveness = NORMAL;
		}

		if (hasTag("raise_effectiveness")) {
			effectiveness++;
		}
		if (hasTag("lower_effectiveness")) {
			effectiveness--;
		}

		return effectiveness;
	}

	@Override
	public void addTag(String tag) {
		tags.add(tag);
	}

	@Override
	public void removeTag(String tag) {
		tags.remove(tag);
	}

	@Override
	public boolean hasTag(String tag) {
		return tags.contains(tag);
	}

	public long getDamageValue() {
		long damageValue = getSum();
		int effectiveness = getEffectiveness();

		if (effectiveness == INEFFECTIVE) {
			effectiveness = 0;
		} else if (effectiveness == ENHANCED) {
			effectiveness *= 2;
		} else if (effectiveness == RESISTED) {
			effectiveness /= 2;
		}

		if (damageValue < 1) {
			damageValue = 1;
		}

		return damageValue;
	}

}
