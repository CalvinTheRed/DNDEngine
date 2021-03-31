package com.dndsuite.core.events;

import java.util.LinkedList;

import com.dndsuite.core.Item;
import com.dndsuite.core.effects.Effect;
import com.dndsuite.core.events.contests.AttackRoll;
import com.dndsuite.core.gameobjects.Entity;
import com.dndsuite.core.gameobjects.GameObject;
import com.dndsuite.dnd.combat.DamageDiceGroup;
import com.dndsuite.dnd.data.DamageType;

public class ItemAttack extends Event {
	public static final String MELEE = "Melee";
	public static final String RANGED = "Ranged";
	public static final String THROWN = "Thrown";
	
	protected Item medium;
	protected String attackType;
	
	public ItemAttack(int attackAbility, Item medium, String attackType) {
		super(null, attackAbility);
		this.medium = medium;
		this.attackType = attackType;
		setName(ItemAttack.getEventID() + " (" + attackType + "," + Entity.getAbility(attackAbility) + ")");
		addTag(ItemAttack.getEventID());
	}
	
	@Override
	public ItemAttack clone() {
		ItemAttack clone = new ItemAttack(eventAbility, medium, attackType);
		cloneDataTo(clone);
		clone.shortrange = shortrange;
		clone.longrange = longrange;
		clone.radius = radius;
		clone.appliedEffects = new LinkedList<Effect>();
		clone.appliedEffects.addAll(appliedEffects);
		
		return clone;
	}
	
	public String getAttackType() {
		return attackType;
	}
	
	public Item getMedium() {
		return medium;
	}
	
	@Override
	public void invokeEvent(Entity source, GameObject target) {
		AttackRoll ar = new AttackRoll(eventAbility, this, target);
		ar.invokeEvent(source, target);
		
		ArmorClassCalculation acc = new ArmorClassCalculation(target);
		acc.invokeEvent(source, target);
		
		if (ar.getRoll() >= acc.getAC()) {
			System.out.println("[JAVA] " + this + " hits! (" + ar.getRoll() + " >= " + acc.getAC() + ") bonus " + ar.getBonus());
			
			DamageCalculation damagecalc = new DamageCalculation(this);
			if (medium != null) {
				damagecalc.addDamageDiceGroup(medium.getDamageDice(attackType));
				
				// Check for versatile damage dice here
				if (source.getMainhand() == source.getOffhand()) {
					for (DamageDiceGroup group : damagecalc.getDamageDice()) {
						group.makeVersatile();
					}
				}
			} else {
				// In the case where medium == null (default unarmed strikes)
				damagecalc.addDamageDiceGroup(new DamageDiceGroup(1, 4, DamageType.BLUDGEONING));
			}
			
			// Check for ability score modifier to damage here
			// This logic assumes the first DamageDiceGroup is
			// the one to receive the bonus from the ability
			// modifier.
			if (source.getMainhand() == medium) {
				damagecalc.getDamageDice().get(0).addBonus(source.getAbilityModifier(eventAbility));
			}
			
			damagecalc.roll(ar);
			damagecalc.invokeEvent(source, target);
			
		} else {
			System.out.println("[JAVA] " + this + " misses! (" + ar.getRoll() + " < " + acc.getAC() + ") bonus " + ar.getBonus());
		}
	}
	
	public static String getEventID() {
		return "Item Attack";
	}

}
