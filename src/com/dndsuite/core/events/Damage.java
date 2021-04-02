package com.dndsuite.core.events;

import com.dndsuite.core.gameobjects.Entity;
import com.dndsuite.core.gameobjects.GameObject;
import com.dndsuite.dnd.combat.DamageDiceGroup;
import com.dndsuite.dnd.data.DamageType;

public class Damage extends Event {
	public static final String WITHSTOOD = "Withstood";

	protected DamageCalculation parent;
	int damage;

	public Damage(DamageCalculation parent) {
		super(null, -1);
		this.parent = parent;
		setName(Damage.getEventID() + " (" + parent.getParent() + ")");
		addTag(Damage.getEventID());
	}

	@Override
	public Damage clone() {
		Damage clone = new Damage(parent);
		cloneDataTo(clone);
		clone.shortrange = shortrange;
		clone.longrange = longrange;
		clone.radius = radius;
		clone.appliedEffects.clear();
		clone.appliedEffects.addAll(appliedEffects);
		clone.targets.clear();
		clone.targets.addAll(targets);

		clone.damage = damage;
		return clone;
	}

	public int getDamage() {
		return damage;
	}
	
	public static String getEventID() {
		return "Damage";
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
	public void invokeEvent(Entity source, GameObject target) {
		damage = 0;
		for (DamageDiceGroup damageDice : parent.getDamageDice()) {
			damage += damageDice.getSum();
		}
		while (target.processEvent(this, source, target))
			;

		target.takeDamage(this);
	}

}
