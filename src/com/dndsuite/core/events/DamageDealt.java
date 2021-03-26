package com.dndsuite.core.events;

import com.dndsuite.core.gameobjects.Entity;
import com.dndsuite.dnd.VirtualBoard;
import com.dndsuite.dnd.combat.DamageDiceGroup;
import com.dndsuite.dnd.data.DamageType;
import com.dndsuite.maths.Vector;

public class DamageDealt extends Event {
	public static final String WITHSTOOD = "Withstood";

	protected DamageCalculation parent;
	int damage;

	public DamageDealt(DamageCalculation parent) {
		super(null);
		this.parent = parent;
		setName(DamageDealt.getEventID() + "(" + parent + ")");
		addTag(DamageDealt.getEventID());
		damage = 0;
		for (DamageDiceGroup damageDice : parent.getDamageDice()) {
			damage += damageDice.getSum();
		}
	}

	@Override
	public void invoke(Entity source, Vector targetPos) {
		Entity target = VirtualBoard.entityAt(targetPos);

		while (source.processEvent(this, source, target) || target.processEvent(this, source, target))
			;

		target.takeDamage(damage);

		if (target.getHealth() <= 0) {
			if (hasTag(WITHSTOOD)) {
				target.setHealth(1);
			} else {
				// TODO: logic for entity death
			}
		}
	}

	public int getDamage() {
		return damage;
	}

	public boolean hasDamageType(DamageType dt) {
		for (DamageDiceGroup group : parent.getDamageDice()) {
			if (group.getDamageType() == dt) {
				return true;
			}
		}
		return false;
	}

	public DamageCalculation getParent() {
		return parent;
	}

	public static String getEventID() {
		return "Damage Dealt";
	}

}
