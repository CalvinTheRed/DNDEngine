package core.events;

import java.util.LinkedList;

import core.gameobjects.Entity;
import maths.Vector;

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
		name = "Armor Class Calculation";
		abilityBonusLimit = Integer.MAX_VALUE;
		baseAC = DEFAULT_BASE_AC;
		bonus = 0;
		acAbilityIndices = new LinkedList<Integer>();
		acAbilityIndices.add(Entity.DEX);
		addTag(Event.SINGLE_TARGET);
		addTag(ArmorClassCalculation.getEventID());
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
