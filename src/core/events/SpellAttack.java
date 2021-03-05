package core.events;

import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import core.gameobjects.Entity;
import dnd.combat.DamageDiceGroup;
import maths.dice.Die;

public class SpellAttack extends AttackRoll {
	public static final String EVENT_TAG_ID = "Spell Attack Roll";

	public SpellAttack(String script, int attackAbility) {
		super(script, attackAbility);
		globals.set("event", CoerceJavaToLua.coerce(this));
		globals.get("define").invoke();
		addTag(EVENT_TAG_ID);
	}

	@Override
	protected void invokeFallout(Entity source) {
		Damage d = new Damage(this);
		DamageDiceGroup damageDice;

		if (getRawRoll() == Die.CRITICAL_HIT) {
			d.addTag(AttackRoll.CRITICAL_HIT);
		}

		globals.set("source", CoerceJavaToLua.coerce(source));
		Varargs va = globals.get("damage").invoke();
		damageDice = (DamageDiceGroup) (va.touserdata(1));

		// spell attack rolls by default do not get additional damage based on the
		// relevant attack ability score

		d.addDamageDiceGroup(damageDice);
		d.invoke(source, null);
		d.clone().invokeAsClone(source, target);

		globals.set("target", CoerceJavaToLua.coerce(target));
		globals.get("additionalEffects").invoke();
	}

}
