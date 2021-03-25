package com.dndsuite.core.events;

import java.util.LinkedList;

import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import com.dndsuite.core.Scriptable;
import com.dndsuite.core.effects.Effect;
import com.dndsuite.core.gameobjects.Entity;
import com.dndsuite.dnd.VirtualBoard;
import com.dndsuite.maths.Vector;

public class Event extends Scriptable {

	public static final int SHORTRANGE = 0;
	public static final int LONGRANGE = 1;
	public static final int DEFAULTRANGE = LONGRANGE;

	// Event area of influence
	public static final String CONE = "Cone";
	public static final String CUBE = "Cube";
	public static final String LINE = "Line";
	public static final String SINGLE_TARGET = "Single Target";
	public static final String SPHERE = "Sphere";

	public static final String SPELL = "Spell";

	protected Vector targetPos;
	protected double shortrange;
	protected double longrange;
	protected double radius;
	protected LinkedList<Effect> appliedEffects;

	public Event(String script) {
		super(script);
		appliedEffects = new LinkedList<Effect>();
	}

	@Override
	public Event clone() {
		Event clone = new Event(script);
		clone.targetPos = targetPos;
		clone.shortrange = shortrange;
		clone.longrange = longrange;
		clone.radius = radius;
		clone.appliedEffects.addAll(appliedEffects);
		clone.tags.clear();
		clone.tags.addAll(tags);
		return clone;
	}

	public void invoke(Entity source, Vector targetPos) {
		System.out.println("[JAVA] " + source + " invokes event " + this);

		LinkedList<Entity> targets = new LinkedList<Entity>();
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
		while (source.processEvent(this, source, target) || target.processEvent(this, source, target))
			;
		globals.get("invokeEvent").invoke();
	}

	public void setRange(double shortrange, double longrange) {
		this.shortrange = shortrange;
		this.longrange = longrange;
	}

	public double[] getRange() {
		return new double[] { shortrange, longrange };
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getRadius() {
		return radius;
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

}
