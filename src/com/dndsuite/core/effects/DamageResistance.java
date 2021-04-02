package com.dndsuite.core.effects;

import com.dndsuite.core.events.DamageCalculation;
import com.dndsuite.core.events.Event;
import com.dndsuite.core.gameobjects.Entity;
import com.dndsuite.core.gameobjects.GameObject;
import com.dndsuite.dnd.combat.DamageDiceGroup;
import com.dndsuite.dnd.data.DamageType;

public class DamageResistance extends Effect {
	protected DamageType damageType;
	
	public DamageResistance(Entity source, Entity target, DamageType damageType) {
		super(null, source, target);
		this.damageType = damageType;
		setName(DamageResistance.getEffectID() + " (" + damageType + ")");
		addTag(DamageResistance.getEffectID());
	}
	
	public DamageType getResistanceType() {
		return damageType;
	}
	
	public static String getEffectID() {
		return "Damage Resistance";
	}
	
	public void processEventSafe(Event e, Entity source, GameObject target) {
		try {
			if (e instanceof DamageCalculation && target == getTarget()) {
				DamageCalculation damagecalc = (DamageCalculation) e;
				for (DamageDiceGroup group : damagecalc.getDamageDice()) {
					if (group.getDamageType() == damageType) {
						damagecalc.applyEffect(this);
						group.grantResistance();
					}
				}
			}
		} catch (Exception ex) {
			System.out.println("[JAVA] Effect " + this + " already applied to event " + e);
		}
	}

}
