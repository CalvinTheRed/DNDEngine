package core.events.contests;

import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import core.events.ArmorClassCalculation;
import core.events.Damage;
import core.events.Event;
import core.gameobjects.Entity;
import dnd.VirtualBoard;
import dnd.combat.DamageDiceGroup;
import maths.Vector;
import maths.dice.Die;

public class AttackRoll extends DiceContest {
	public static final String EVENT_TAG_ID = "Attack Roll";

	public static final String MELEE = "Melee";
	public static final String RANGED = "Ranged";
	public static final String THROWN = "Thrown";

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
		addTag(EVENT_TAG_ID);
		if (globals != null) {
			globals.set("event", CoerceJavaToLua.coerce(this));
			globals.get("define").invoke();
		}

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
		ArmorClassCalculation acc = new ArmorClassCalculation(source);
		source.processEvent(acc, source, source);
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
		Damage d = new Damage(this);

		if (getRawRoll() == Die.CRITICAL_HIT) {
			d.addTag(CRITICAL_HIT);
		} else if (getRawRoll() == Die.CRITICAL_FAIL) {
			d.addTag(CRITICAL_MISS);
		}

		globals.set("source", CoerceJavaToLua.coerce(source));
		Varargs va = globals.get("damage").invoke();

		int index = 1;
		while (va.isuserdata(index)) {
			d.addDamageDiceGroup((DamageDiceGroup) va.touserdata(index));
			index++;
		}

		d.invoke(source, null);
		d.clone().invokeAsClone(source, target);

		globals.set("target", CoerceJavaToLua.coerce(target));
		globals.get("additionalEffects").invoke();
	}

	protected void invokeFalloutOnMiss(Entity source) {

	}

}
