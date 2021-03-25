package com.dndsuite.core.events.contests;

import java.util.LinkedList;

import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import com.dndsuite.core.events.Damage;
import com.dndsuite.core.events.DiceCheckCalculation;
import com.dndsuite.core.gameobjects.Entity;
import com.dndsuite.dnd.VirtualBoard;
import com.dndsuite.dnd.combat.DamageDiceGroup;
import com.dndsuite.maths.Vector;

public class SavingThrow extends DiceContest {
	public static final String HALF_DAMAGE_ON_PASS = "Half Damage on Pass";

	protected Damage d;
	protected LinkedList<Entity> targets;
	protected int saveAbility;
	protected int dcAbility;

	public SavingThrow(String script, int dcAbility) {
		super(script);
		targets = new LinkedList<Entity>();
		this.dcAbility = dcAbility;
		addTag(SavingThrow.getEventID());
		if (globals != null) {
			globals.set("event", CoerceJavaToLua.coerce(this));
			globals.get("define").invoke();
		}
		d = new Damage(this);
	}

	@Override
	public SavingThrow clone() {
		SavingThrow clone = new SavingThrow(script, dcAbility);
		clone.targetPos = targetPos;
		clone.shortrange = shortrange;
		clone.longrange = longrange;
		clone.radius = radius;
		clone.appliedEffects.addAll(appliedEffects);
		clone.tags.clear();
		clone.tags.addAll(tags);
		clone.d = d.clone();
		clone.targets.addAll(targets);
		clone.saveAbility = saveAbility;
		clone.dcAbility = dcAbility;
		return clone;
	}

	@Override
	public void invoke(Entity source, Vector targetPos) {
		System.out.println("[JAVA] " + source + " invokes Event " + this);

		if (hasTag(CONE)) {
			targets.addAll(VirtualBoard.entitiesInCone2(targetPos, source.getRot(), getRange()[DEFAULTRANGE]));
		} else if (hasTag(CUBE)) {
			targets.addAll(VirtualBoard.entitiesInCube(targetPos, source.getRot(), radius));
		} else if (hasTag(LINE)) {
			targets.addAll(VirtualBoard.entitiesInLine(targetPos, source.getRot(), getRange()[DEFAULTRANGE], radius));
		} else if (hasTag(SINGLE_TARGET)) {
			targets.add(VirtualBoard.entityAt(targetPos));
		} else if (hasTag(SPHERE)) {
			targets.addAll(VirtualBoard.entitiesInSphere(targetPos, radius));
		} else {
			System.out
					.println("[JAVA] ERR: event " + this + " has no targeting tags! (Choose from among the following)");
			System.out.println("       Event.CONE, Event.CUBE, Event.LINE, Event.SINGLE_TARGET, Event.SPHERE");
			return;
		}

		Varargs va = globals.get("damage").invoke();

		int index = 1;
		while (va.isuserdata(index)) {
			d.addDamageDiceGroup((DamageDiceGroup) va.touserdata(index));
			index++;
		}
		d.invoke(source, null);

		// Give the source a chance to modify the Event before it gets cloned to each of
		// its targets
		// TODO: consider adding preProcess(Event e) method?

		// Clone this event to each of its targets
		for (Entity target : targets) {
			clone().invokeAsClone(source, target);
		}

	}

	public void invokeAsClone(Entity source, Entity target) {
		globals.set("source", CoerceJavaToLua.coerce(source));
		globals.set("target", CoerceJavaToLua.coerce(target));
		globals.set("event", CoerceJavaToLua.coerce(this));
		globals.get("define").invoke();
		while (source.processEvent(this, source, target) || target.processEvent(this, source, target)) {
		}

		roll();

		DiceCheckCalculation dcc = new DiceCheckCalculation(source, dcAbility);
		source.processEvent(dcc, source, source);
		addBonus(target.getAbilityModifier(saveAbility));
		if (getRoll() < dcc.getDC()) {
			System.out.println("[JAVA] " + target + " failed its saving throw! (" + getRawRoll() + "+" + bonus + ":"
					+ dcc.getDC() + ")");
			d.clone().invokeAsClone(source, target);
			globals.set("target", CoerceJavaToLua.coerce(target));
			globals.get("additionalEffects").invoke();
		} else {
			System.out.println("[JAVA] " + target + " passed its saving throw! (" + getRawRoll() + "+" + bonus + ":"
					+ dcc.getDC() + ")");
			if (hasTag(HALF_DAMAGE_ON_PASS)) {
				d.clone().invokeHalvedAsClone(source, target);
			}
			globals.set("target", CoerceJavaToLua.coerce(target));
			globals.get("additionalEffectsOnPass").invoke();
		}

	}

	@Override
	protected void invokeFallout(Entity source) {
		// TODO Auto-generated method stub
	}

	public int getSaveAbility() {
		return saveAbility;
	}

	public int getDCAbility() {
		return dcAbility;
	}

	public void setSaveAbility(int saveAbility) {
		this.saveAbility = saveAbility;
	}

	public static String getEventID() {
		return "Saving Throw";
	}

}
