package com.dndsuite.core.effects;

import com.dndsuite.core.events.DamageCalculation;
import com.dndsuite.core.events.Event;
import com.dndsuite.core.gameobjects.Entity;
import com.dndsuite.core.gameobjects.GameObject;
import com.dndsuite.dnd.combat.DamageDiceGroup;
import com.dndsuite.dnd.data.DamageType;

public class DamageImmunity extends Effect {
	protected DamageType damageType;
	
	public DamageImmunity(Entity source, Entity target, DamageType damageType) {
		super(null, source, target);
		this.damageType = damageType;
		setName(DamageImmunity.getEffectID() + " (" + damageType + ")");
		addTag(DamageImmunity.getEffectID());
	}
	
	public DamageType getImmunityType() {
		return damageType;
	}
	
	public static String getEffectID() {
		return "Damage Immunity";
	}
	
	public void processEventSafe(Event e, Entity source, GameObject target) {
		try {
			if (e instanceof DamageCalculation && target == getTarget()) {
				DamageCalculation damagecalc = (DamageCalculation) e;
				for (DamageDiceGroup group : damagecalc.getDamageDice()) {
					if (group.getDamageType() == damageType) {
						damagecalc.applyEffect(this);
						group.grantImmunity();
					}
				}
			}
		} catch (Exception ex) {
			System.out.println("[JAVA] Effect " + this + " already applied to event " + e);
		}
	}

}
