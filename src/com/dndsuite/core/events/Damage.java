package com.dndsuite.core.events;

import com.dndsuite.core.gameobjects.Entity;
import com.dndsuite.core.gameobjects.GameObject;
import com.dndsuite.dnd.combat.DamageDiceGroup;
import com.dndsuite.dnd.data.DamageType;
import com.dndsuite.maths.Vector;

public class Damage extends Event {
	public static final String WITHSTOOD = "Withstood";

	protected DamageCalculation parent;
	int damage;

	public Damage(DamageCalculation parent) {
		super(null, -1);
		this.parent = parent;
		setName(Damage.getEventID() + "(" + parent + ")");
		addTag(Damage.getEventID());
	}

	@Override
	public Damage clone() {
		Damage clone = (Damage) super.clone();
		clone.parent = parent;
		clone.damage = damage;
		return clone;
	}

	public int getDamage() {
		return damage;
	}

	public DamageCalculation getParent() {
		return parent;
	}

	public boolean hasDamageType(DamageType dt) {
		for (DamageDiceGroup group : parent.getDamageDice()) {
			if (group.getDamageType() == dt) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void invoke(Entity source, Vector targetPos) {
		damage = 0;
		for (DamageDiceGroup damageDice : parent.getDamageDice()) {
			damage += damageDice.getSum();
		}
		super.invoke(source, targetPos);
	}

	@Override
	public void invokeEvent(Entity source, GameObject target) {
		target.takeDamage(damage);
	}

	public static String getEventID() {
		return "Damage Dealt";
	}

}
