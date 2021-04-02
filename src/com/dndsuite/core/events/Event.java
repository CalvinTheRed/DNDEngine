package com.dndsuite.core.events;

import java.util.LinkedList;

import org.luaj.vm2.Varargs;

import com.dndsuite.core.Scriptable;
import com.dndsuite.core.effects.Effect;
import com.dndsuite.core.gameobjects.Entity;
import com.dndsuite.core.gameobjects.GameObject;
import com.dndsuite.dnd.VirtualBoard;
import com.dndsuite.maths.Vector;

public class Event extends Scriptable {
	public static final String SPELL = "Spell";

	protected LinkedList<Effect> appliedEffects;
	protected LinkedList<GameObject> targets;
	protected int eventAbility;
	protected double shortrange;
	protected double longrange;
	protected double radius;

	public Event(String script, int eventAbility) {
		super(script);
		this.eventAbility = eventAbility;
	}

	public void addTarget(GameObject target) {
		targets.add(target);
	}

	public void addTargets(LinkedList<GameObject> targets) {
		this.targets.addAll(targets);
	}

	public void applyEffect(Effect e) throws Exception {
		// TODO: verify two like objects cannot both be applied
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
		clone.appliedEffects.clear();
		clone.appliedEffects.addAll(appliedEffects);
		clone.targets.clear();
		clone.targets.addAll(targets);
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

		try {
			passToLua("targetPos", targetPos);
			Varargs va = invokeFromLua("targets");
			Object userdata = va.touserdata(1);
			
			if (userdata instanceof GameObject) {
				targets.add((GameObject) userdata);
			} else if (userdata instanceof LinkedList<?>) {
				targets.addAll((LinkedList<GameObject>) userdata);
			}
			
			for (GameObject target : targets) {
				Event clone = clone();
				clone.invokeAsClone(source, target);
			}
		} catch (Exception ex) {
			targets.addAll(targets(targetPos));
			for (GameObject target : targets) {
				clone().invokeAsClone(source, target);
			}
		}
		
	}

	public void invokeAsClone(Entity source, GameObject target) {
		while (source.processEvent(this, source, target) || target.processEvent(this, source, target))
			;

		try {
			passToLua("source", source);
			passToLua("target", target);
			invokeFromLua("invokeEvent");
		} catch (Exception ex) {
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

	@Override
	protected void setup() {
		super.setup();
		appliedEffects = new LinkedList<Effect>();
		targets = new LinkedList<GameObject>();
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
