package com.dndsuite.core.events;

import java.util.LinkedList;

import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import com.dndsuite.core.Scriptable;
import com.dndsuite.core.effects.Effect;
import com.dndsuite.core.gameobjects.Entity;
import com.dndsuite.core.gameobjects.GameObject;
import com.dndsuite.dnd.VirtualBoard;
import com.dndsuite.maths.Vector;

public class Event extends Scriptable {
	public static final String SPELL = "Spell";
	
	// TODO: create AbilityScoreCaculation event
	// to avoid the issue of abilityScores[] vs
	// baseAbilityScores distinction! Define the
	// base scores, then let Effect objects
	// determine the emergent scores.

	protected int eventAbility;
	protected double shortrange;
	protected double longrange;
	protected double radius;
	protected LinkedList<Effect> appliedEffects;
	protected LinkedList<GameObject> targets;

	public Event(String script, int eventAbility) {
		super(script);
		this.eventAbility = eventAbility;
		appliedEffects = new LinkedList<Effect>();
		targets = new LinkedList<GameObject>();
	}

	public void addTarget(GameObject target) {
		targets.add(target);
	}

	public void addTargets(LinkedList<GameObject> targets) {
		this.targets.addAll(targets);
	}

	public void applyEffect(Effect e) throws Exception {
		if (appliedEffects.contains(e)) {
			throw new Exception("Effect already applied");
		}
		System.out.println("[JAVA] Applying effect " + e + " from " + e.getSource() + " to event " + this);
		appliedEffects.add(e);
	}

	public void clearAppliedEffects() {
		appliedEffects.clear();
	}

	@Override
	public Event clone() {
		Event clone = new Event(script, eventAbility);
		cloneDataTo(clone);
		clone.shortrange = shortrange;
		clone.longrange = longrange;
		clone.radius = radius;
		clone.appliedEffects = new LinkedList<Effect>();
		clone.appliedEffects.addAll(appliedEffects);
		return clone;
	}

	public LinkedList<Effect> getEffects() {
		return appliedEffects;
	}

	public int getEventAbility() {
		return eventAbility;
	}

	public double getRadius() {
		return radius;
	}

	public double[] getRange() {
		return new double[] { shortrange, longrange };
	}

	public LinkedList<GameObject> getTargets() {
		return targets;
	}

	@SuppressWarnings("unchecked")
	public void invoke(Entity source, Vector targetPos) {
		System.out.println("[JAVA] " + source + " invokes event " + this);

		if (globals != null) {
			globals.set("targetPos", CoerceJavaToLua.coerce(targetPos));
			Varargs va = globals.get("targets").invoke();
			Object userdata = va.touserdata(1);
			if (userdata instanceof GameObject) {
				targets.add((GameObject) userdata);
			} else if (userdata instanceof LinkedList<?>) {
				targets.addAll((LinkedList<GameObject>) userdata);
			}
		} else {
			targets.addAll(targets(targetPos));
		}

		for (GameObject target : targets) {
			Event clone = clone();
			clone.globals.set("globals", CoerceJavaToLua.coerce(globals));
			clone().invokeAsClone(source, target);
		}
	}

	public void invokeAsClone(Entity source, GameObject target) {
		while (source.processEvent(this, source, target) || target.processEvent(this, source, target))
			;

		if (globals != null) {
			globals.set("source", CoerceJavaToLua.coerce(source));
			globals.set("target", CoerceJavaToLua.coerce(target));
			globals.get("invokeEvent").invoke();
		} else {
			invokeEvent(source, target);
		}
	}

	public void invokeEvent(Entity source, GameObject target) {
		/*
		 * This function represents the "invokeEvent()" function in Lua scripts, defined
		 * within Java. This function shall be overridden by child classes of Event.
		 */
	}

	public boolean removeTarget(GameObject target) {
		return targets.remove(target);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public void setRange(double shortrange, double longrange) {
		this.shortrange = shortrange;
		this.longrange = longrange;
	}

	protected LinkedList<GameObject> targets(Vector targetPos) {
		/*
		 * This function represents the "targets()" function in Lua scripts, defined
		 * within Java. This function can be overridden by child classes of Event.
		 */
		LinkedList<GameObject> targets = new LinkedList<GameObject>();
		targets.add(VirtualBoard.nearestObject(targetPos, new String[] {}));
		return targets;
	}

}
