package com.dndsuite.core.events;

import java.util.LinkedList;

import com.dndsuite.core.Item;
import com.dndsuite.core.gameobjects.Entity;
import com.dndsuite.maths.Vector;

public class ArmorClassCalculation extends Event {
	private static final int DEFAULT_BASE_AC = 10;

	protected Entity subject;
	protected LinkedList<Integer> acAbilityIndices;
	protected int abilityBonusLimit;
	protected int baseAC;
	protected int bonus;

	public ArmorClassCalculation(Entity subject) {
		super(null, -1);
		this.subject = subject;
		acAbilityIndices = new LinkedList<Integer>();
		setName(ArmorClassCalculation.getEventID());
		addTag(ArmorClassCalculation.getEventID());
	}

	@Override
	public ArmorClassCalculation clone() {
		ArmorClassCalculation clone = (ArmorClassCalculation) super.clone();
		clone.subject = subject;
		clone.acAbilityIndices = new LinkedList<Integer>();
		clone.acAbilityIndices.addAll(acAbilityIndices);
		clone.abilityBonusLimit = abilityBonusLimit;
		clone.baseAC = baseAC;
		clone.bonus = bonus;
		return clone;
	}

	@Override
	public void invoke(Entity source, Vector targetPos) {
		acAbilityIndices.add(Entity.DEX);
		bonus = 0;

		if (subject.getArmor() == null) {
			baseAC = DEFAULT_BASE_AC;
			abilityBonusLimit = Integer.MAX_VALUE;
		} else {
			Item armor = subject.getArmor();
			baseAC = armor.getAC();
			abilityBonusLimit = armor.getACAbilityBonusLimit();
		}
		super.invoke(source, targetPos);
	}

	public void addACAbilityIndex(int index) {
		acAbilityIndices.add(index);
	}

	public void addBonus(int bonus) {
		this.bonus += bonus;
	}

	public void clearACAbilityIndices() {
		acAbilityIndices.clear();
	}

	public int getAC() {
		int ac = baseAC + bonus;
		for (int abilityIndex : acAbilityIndices) {
			ac += Math.min(abilityBonusLimit, subject.getAbilityModifier(abilityIndex));
		}
		return ac;
	}

	public int getBaseAC() {
		return baseAC;
	}

	public int getBonus() {
		return bonus;
	}

	public Entity getSubject() {
		return subject;
	}

	public void resetACAbilityIndices() {
		clearACAbilityIndices();
		addACAbilityIndex(Entity.DEX);
	}

	public void setAbilityBonusLimit(int limit) {
		abilityBonusLimit = limit;
	}

	public void setBaseAC(int baseAC) {
		this.baseAC = baseAC;
	}

	public static String getEventID() {
		return "Armor Class Calculation";
	}

}
