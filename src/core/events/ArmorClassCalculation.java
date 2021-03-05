package core.events;

import java.util.LinkedList;

import core.gameobjects.Entity;
import maths.Vector;

public class ArmorClassCalculation extends Event {
	public static final String EVENT_TAG_ID = "Armor Class Calculation";

	protected Entity parent;
	protected LinkedList<Integer> acAbilityIndices;
	protected int abilityBonusLimit;
	protected int baseAC;
	protected int bonus;

	public ArmorClassCalculation(Entity parent) {
		super(null);
		this.parent = parent;
		name = "Armor Class Calculation";
		abilityBonusLimit = Integer.MAX_VALUE;
		baseAC = 10;
		bonus = 0;
		acAbilityIndices = new LinkedList<Integer>();
		acAbilityIndices.add(Entity.DEX);
		addTag(Event.SINGLE_TARGET);
		addTag(ArmorClassCalculation.EVENT_TAG_ID);
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

	@Override
	public void invoke(Entity source, Vector targetPos) {
	}

}
