package core.events;

import java.util.LinkedList;

import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import core.Effect;
import core.Scriptable;
import core.gameobjects.Entity;
import dnd.VirtualBoard;
import maths.Vector;

/**
 * An abstract class which represents the act of doing something (casting a
 * spell, making a weapon attack, making a saving throw, etc)
 * 
 * @author calvi
 *
 */
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

	protected Vector targetPos;
	protected String name;
	protected double shortrange;
	protected double longrange;
	protected double radius;
	protected LinkedList<Effect> appliedEffects;
	protected LinkedList<String> tags;

	public Event(String script, String targetTag) {
		super(script);
		appliedEffects = new LinkedList<Effect>();
		tags = new LinkedList<String>();
		addTag(targetTag);
		if (globals != null) {
			globals.set("event", CoerceJavaToLua.coerce(this));
			globals.get("define").invoke();
		}
	}

	private Event(String script) {
		super(script);
		appliedEffects = new LinkedList<Effect>();
		tags = new LinkedList<String>();
		if (globals != null) {
			globals.set("event", CoerceJavaToLua.coerce(this));
			globals.get("define").invoke();
		}
	}

	@Override
	public Event clone() {
		Event clone = new Event(script, name);
		clone.targetPos = targetPos;
		clone.shortrange = shortrange;
		clone.longrange = longrange;
		clone.radius = radius;
		clone.appliedEffects.addAll(appliedEffects);
		clone.tags.addAll(tags);
		return clone;
	}

	public void invoke(Entity source, Vector targetPos) {
		System.out.println("[JAVA] " + source + " invokes Event " + this);

		LinkedList<Entity> targets = new LinkedList<Entity>();
		if (hasTag("Cone")) {
			targets.addAll(VirtualBoard.entitiesInCone2(targetPos, source.getRot(), getRange()[DEFAULTRANGE]));
		} else if (hasTag("Cube")) {
			targets.addAll(VirtualBoard.entitiesInCube(targetPos, source.getRot(), radius));
		} else if (hasTag("Line")) {
			targets.addAll(VirtualBoard.entitiesInLine(targetPos, source.getRot(), getRange()[DEFAULTRANGE], radius));
		} else if (hasTag("Single Target")) {
			targets.add(VirtualBoard.entityAt(targetPos));
		} else if (hasTag("Sphere")) {
			targets.addAll(VirtualBoard.entitiesInSphere(targetPos, radius));
		} else {
			System.out.println("[JAVA] ERR: event " + this + " has no targeting tags! (Choose from among the following)");
			System.out.println("       \"Cone\",\"Cube\",\"Line\",\"Single Target\",\"Sphere\"");
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

	private void invokeAsClone(Entity source, Entity target) {
		globals.set("source", CoerceJavaToLua.coerce(source));
		globals.set("target", CoerceJavaToLua.coerce(target));
		globals.set("event", CoerceJavaToLua.coerce(this));
		globals.get("define").invoke();
		while (source.processEvent(this, source, target) || target.processEvent(this, source, target))
			;
		globals.get("invokeEvent").invoke();
	}

	public void addTag(String tag) {
		tags.add(tag);
	}

	public boolean hasTag(String tag) {
		return tags.contains(tag);
	}

	public void setName(String name) {
		this.name = name;
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
		System.out.println("[JAVA] Applying effect " + e + " from " + e.getSource() + " to " + this);
		appliedEffects.add(e);
	}

	public void clearAppliedEffects() {
		appliedEffects.clear();
	}

	@Override
	public String toString() {
		return name;
	}
}
