package com.dndsuite.core.events;

import java.util.LinkedList;

import com.dndsuite.core.Item;
import com.dndsuite.core.gameobjects.Entity;
import com.dndsuite.maths.Vector;

public class ArmorClassCalculation extends Event {
	private static final int DEFAULT_BASE_AC = 10;

	protected Entity parent;
	protected LinkedList<Integer> acAbilityIndices;
	protected int abilityBonusLimit;
	protected int baseAC;
	protected int bonus;

	public ArmorClassCalculation(Entity parent) {
		super(null);
		this.parent = parent;
		acAbilityIndices = new LinkedList<Integer>();
		acAbilityIndices.add(Entity.DEX);
		bonus = 0;
		setName(ArmorClassCalculation.getEventID());
		addTag(Event.SINGLE_TARGET);
		addTag(ArmorClassCalculation.getEventID());

		if (parent.getArmor() == null) {
			baseAC = DEFAULT_BASE_AC;
			abilityBonusLimit = Integer.MAX_VALUE;
		} else {
			Item armor = parent.getArmor();
			baseAC = armor.getAC();
			abilityBonusLimit = armor.getACAbilityBonusLimit();
		}
	}

	public void addBonus(int bonus) {
		this.bonus += bonus;
	}

	public void addACAbilityIndex(int index) {
		acAbilityIndices.add(index);
	}

	public void clearACAbilityIndices() {
		acAbilityIndices.clear();
	}

	public int getAC() {
		int ac = baseAC + bonus;
		for (int abilityIndex : acAbilityIndices) {
			ac += Math.min(abilityBonusLimit, parent.getAbilityModifier(abilityIndex));
		}
		return ac;
	}

	public int getBaseAC() {
		return baseAC;
	}

	public int getBonus() {
		return bonus;
	}

	public Entity getParent() {
		return parent;
	}

	public void setBaseAC(int baseAC) {
		this.baseAC = baseAC;
	}

	public void setAbilityBonusLimit(int limit) {
		abilityBonusLimit = limit;
	}

	public static String getEventID() {
		return "Armor Class Calculation";
	}

	@Override
	public void invoke(Entity source, Vector targetPos) {
	}

}
