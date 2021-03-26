package com.dndsuite.core.events.contests;

import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import com.dndsuite.core.events.ArmorClassCalculation;
import com.dndsuite.core.events.DamageCalculation;
import com.dndsuite.core.events.Event;
import com.dndsuite.core.gameobjects.Entity;
import com.dndsuite.dnd.VirtualBoard;
import com.dndsuite.dnd.combat.DamageDiceGroup;
import com.dndsuite.maths.Vector;
import com.dndsuite.maths.dice.Die;

public class AttackRoll extends DiceContest {
	public static final String MELEE = "Melee Attack";
	public static final String RANGED = "Ranged Attack";
	public static final String THROWN = "Thrown Attack";

	public static final String CRITICAL_HIT = "Critical Hit";
	public static final String CRITICAL_MISS = "Critical Miss";

	protected String attackType;
	protected int attackAbility;
	protected Entity target;

	public AttackRoll(String script, int attackAbility) {
		super(script);
		this.attackAbility = attackAbility;
		setRadius(0.0);
		addTag(Event.SINGLE_TARGET);
		addTag(AttackRoll.getEventID());
	}

	public int getAttackAbility() {
		return attackAbility;
	}

	public String getAttackType() {
		return attackType;
	}

	public void setAttackType(String attackType) {
		this.attackType = attackType;
	}

	@Override
	public void invoke(Entity source, Vector targetPos) {
		System.out.println("[JAVA] " + source + " invokes Event " + this);

		target = VirtualBoard.entityAt(targetPos);

		while (source.processEvent(this, source, target) || target.processEvent(this, source, target))
			;

		// apply relevant ability score modifier as attack roll bonus
		addBonus(source.getAbilityModifier(attackAbility));
		roll();

		/*
		 * TODO: create a second after-the-fact wave of event processing while
		 * (source.processEvent(this, source, target) || target.processEvent(this,
		 * source, target)) ;
		 */
		ArmorClassCalculation acc = new ArmorClassCalculation(target);
		source.processEvent(acc, source, target);
		if (getRoll() >= acc.getAC()) {
			System.out.println("[JAVA] Attack roll hit! (" + getRawRoll() + "+" + bonus + ":" + acc.getAC() + ")");
			invokeFallout(source);
		} else {
			System.out.println("[JAVA] Attack roll miss! (" + getRawRoll() + "+" + bonus + ":" + acc.getAC() + ")");
			/*
			 * Some attack roll events, such as the Ice Knife spell, have secondary
			 * consequences which come to pass even if the attack roll misses its target.
			 * These globals calls provide an opportunity to enact such behavior.
			 */
			if (globals != null) {
				globals.set("source", CoerceJavaToLua.coerce(source));
				globals.set("target", CoerceJavaToLua.coerce(target));
				globals.get("additionalEffectsOnMiss").invoke();
			} else {
				// TODO: is this ever going to be used?
				invokeFalloutOnMiss(source);
			}

		}
	}

	@Override
	protected void invokeFallout(Entity source) {
		DamageCalculation dc = new DamageCalculation(this);

		if (getRawRoll() == Die.CRITICAL_HIT) {
			dc.addTag(CRITICAL_HIT);
		} else if (getRawRoll() == Die.CRITICAL_FAIL) {
			dc.addTag(CRITICAL_MISS);
		}

		globals.set("source", CoerceJavaToLua.coerce(source));
		Varargs va = globals.get("damage").invoke();

		int index = 1;
		while (va.isuserdata(index)) {
			dc.addDamageDiceGroup((DamageDiceGroup) va.touserdata(index));
			index++;
		}

		dc.invoke(source, null);
		dc.clone().invokeAsClone(source, target);

		globals.set("target", CoerceJavaToLua.coerce(target));
		globals.get("additionalEffects").invoke();
	}

	protected void invokeFalloutOnMiss(Entity source) {
		// TODO: is this ever going to get used internally, or will lua scripts always
		// drive this idea?
	}

	public static String getEventID() {
		return "Attack Roll";
	}

}
