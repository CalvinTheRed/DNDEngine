package com.dndsuite.core.events;

import java.util.LinkedList;

import com.dndsuite.core.Item;
import com.dndsuite.core.effects.Effect;
import com.dndsuite.core.gameobjects.Entity;
import com.dndsuite.core.gameobjects.GameObject;

public class ArmorClassCalculation extends Event {
	private static final int DEFAULT_BASE_AC = 10;

	protected GameObject subject;
	protected LinkedList<Integer> acAbilityIndices;
	protected int abilityBonusLimit;
	protected int baseAC;
	protected int bonus;

	public ArmorClassCalculation(GameObject subject) {
		super(null, -1);
		this.subject = subject;
		acAbilityIndices = new LinkedList<Integer>();
		setName(ArmorClassCalculation.getEventID());
		addTag(ArmorClassCalculation.getEventID());
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

	@Override
	public ArmorClassCalculation clone() {
		ArmorClassCalculation clone = new ArmorClassCalculation(subject);
		cloneDataTo(clone);
		clone.shortrange = shortrange;
		clone.longrange = longrange;
		clone.radius = radius;
		clone.appliedEffects = new LinkedList<Effect>();
		clone.appliedEffects.addAll(appliedEffects);

		clone.acAbilityIndices.clear();
		clone.acAbilityIndices.addAll(acAbilityIndices);
		clone.abilityBonusLimit = abilityBonusLimit;
		clone.baseAC = baseAC;
		clone.bonus = bonus;
		return clone;
	}

	public int getAC() {
		if (!(subject instanceof Entity)) {
			return 0;
		}
		int ac = baseAC + bonus;
		for (int abilityIndex : acAbilityIndices) {
			ac += Math.min(abilityBonusLimit, ((Entity) subject).getAbilityModifier(abilityIndex));
		}
		return ac;
	}

	public int getBaseAC() {
		return baseAC;
	}

	public int getBonus() {
		return bonus;
	}

	public GameObject getSubject() {
		return subject;
	}

	@Override
	public void invokeEvent(Entity source, GameObject target) {
		acAbilityIndices.add(Entity.DEX);
		bonus = 0;

		if (subject instanceof Entity) {
			if (((Entity) subject).getArmor() == null) {
				baseAC = DEFAULT_BASE_AC;
				abilityBonusLimit = Integer.MAX_VALUE;
			} else {
				Item armor = ((Entity) subject).getArmor();
				baseAC = armor.getAC();
				abilityBonusLimit = armor.getACAbilityBonusLimit();
			}
		}

		while (target.processEvent(this, source, target))
			;
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
