package dnd.events;

import java.util.LinkedList;

import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import dnd.effects.Effect;
import engine.Scriptable;
import engine.VirtualBoard;
import gameobjects.entities.Entity;
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

	/**
	 * Constructor for class Event TODO: add parameter for target tag? cone/cube/etc
	 * 
	 * @param name ({@code String}) the name of the Event
	 */
	public Event(String script, String name, String targetTag) {
		super(script);
		this.name = name;
		appliedEffects = new LinkedList<Effect>();
		tags = new LinkedList<String>();
		addTag(targetTag);
	}

	private Event(String script, String name) {
		super(script);
		this.name = name;
		appliedEffects = new LinkedList<Effect>();
		tags = new LinkedList<String>();
	}

	@Override
	public Event clone() {
		Event clone = new Event(script, name);
		clone.targetPos = targetPos;
		clone.shortrange = shortrange;
		clone.longrange = longrange;
		clone.radius = radius;
		for (Effect effect : appliedEffects) {
			clone.appliedEffects.add(effect);
		}
		for (String tag : tags) {
			clone.addTag(tag);
		}
		return clone;
	}

	// TODO: figure out how to interface with this in luaj
	public void addTags(String... taglist) {
		for (String tag : taglist) {
			tags.add(tag);
		}
	}

	public void addTag(String tag) {
		tags.add(tag);
	}

	// TODO: figure out how to interface with this in luaj
	public boolean checkForTags(String... taglist) {
		for (String tag : taglist) {
			if (!tags.contains(tag)) {
				return false;
			}
		}
		return true;
	}

	public boolean checkForTag(String tag) {
		return tags.contains(tag);
	}

	/**
	 * This function sets the range (short and long) of the Event
	 * 
	 * @param shortrange ({@code double}) the short range of the spell
	 * @param longrange  ({@code double}) the long range of the spell
	 */
	public void setRange(double shortrange, double longrange) {
		this.shortrange = shortrange;
		this.longrange = longrange;
	}

	/**
	 * This function returns an array of the short and long range of the Event
	 * 
	 * @return {@code double[2]} range
	 */
	public double[] getRange() {
		return new double[] { shortrange, longrange };
	}

	/**
	 * This function sets the radius of the Event
	 * 
	 * @param radius ({@code double}) the radius of the Event
	 */
	public void setRadius(int radius) {
		this.radius = radius;
	}

	/**
	 * This function returns the radius of the Event
	 * 
	 * @return ({@code double}) radius
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * This function appends the passed Effect to the contained list of Effects.
	 * This allows the Event to know which Effects have already modified it, and
	 * thus should not be able to modify it again
	 * 
	 * @param e ({@code Effect}) the Effect to be added
	 * @throws Exception if the Effect e is already contained in the Event
	 */
	public void applyEffect(Effect e) throws Exception {
		if (appliedEffects.contains(e)) {
			throw new Exception("Effect already applied");
		}
		System.out.println("[JAVA] Applying effect " + e + " from " + e.getSource() + " to " + this);
		appliedEffects.add(e);
	}

	/**
	 * This function clears all entries from the contained list of Effects
	 */
	public void clearAppliedEffects() {
		appliedEffects.clear();
	}

	/**
	 * This function represents acting out the Event. The behavior of this function
	 * will differ between different Event objects, as it is meant to be overridden
	 * and then called as super() at the beginning of the new function
	 * 
	 * @param source    ({@code Entity}) the Entity which is initiating this Event
	 * @param targetPos ({@code Vector}) the coordinate which represent the origin
	 *                  of the Event's area of effect (the point of a cone, the
	 *                  center of a cube, the beginning of a line, the exact
	 *                  coordinate of a single creature, or the center of a sphere)
	 */
	public void invoke(Entity source, Vector targetPos) {
		System.out.println("[JAVA] " + source + " invokes Event " + this);

		LinkedList<Entity> targets = new LinkedList<Entity>();
		if (checkForTags("Cone")) {
			targets.addAll(VirtualBoard.entitiesInCone2(targetPos, source.getRot(), getRange()[DEFAULTRANGE]));
		} else if (checkForTags("Cube")) {
			targets.addAll(VirtualBoard.entitiesInCube(targetPos, source.getRot(), radius));
		} else if (checkForTags("Line")) {
			targets.addAll(VirtualBoard.entitiesInLine(targetPos, source.getRot(), getRange()[DEFAULTRANGE], radius));
		} else if (checkForTags("Single Target")) {
			targets.add(VirtualBoard.entityAt(targetPos));
		} else if (checkForTags("Sphere")) {
			targets.addAll(VirtualBoard.entitiesInSphere(targetPos, radius));
		} else {
			System.out
					.println("[JAVA] ERR: event " + this + " has no targeting tags! (Choose from among the following)");
			System.out.println("[JAVA] \"Cone\",\"Cube\",\"Line\",\"Single Target\",\"Sphere\"");
			return;
		}

		// Give the source a chance to modify the Event before it gets cloned to each of
		// its targets
		// TODO: consider adding preProcess(Event e) method?

		// Clone this event to each of its targets
		for (Entity target : targets) {
			clone().invokeClone(source, target);
		}

	}

	public void invokeClone(Entity source, Entity target) {
		while (source.processEvent(this, source, target) || target.processEvent(this, source, target))
			;
		globals.set("source", CoerceJavaToLua.coerce(source));
		globals.set("target", CoerceJavaToLua.coerce(target));
		globals.get("invokeEvent").invoke();
	}

	@Override
	public String toString() {
		return name;
	}
}
