package dnd.events;

import java.util.LinkedList;

import dnd.data.EventShape;
import dnd.effects.Effect;
import engine.Scriptable;
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

	protected LinkedList<Effect> appliedEffects;
	protected Vector targetPos;
	protected String name;
	protected double[] range;
	protected EventShape shape;
	protected double radius;

	/**
	 * Constructor for class Event
	 * 
	 * @param name ({@code String}) the name of the Event
	 */
	public Event(String script, String name) {
		super(script);
		this.name = name;
		appliedEffects = new LinkedList<Effect>();
		range = new double[2];
	}

	/**
	 * This function sets the range (short and long) of the Event
	 * 
	 * @param shortrange ({@code double}) the short range of the spell
	 * @param longrange  ({@code double}) the long range of the spell
	 */
	public void setRange(double shortrange, double longrange) {
		range[SHORTRANGE] = shortrange;
		range[LONGRANGE] = longrange;
	}

	/**
	 * This function returns an array of the short and long range of the Event
	 * 
	 * @return {@code double[2]} range
	 */
	public double[] getRange() {
		return range;
	}

	/**
	 * This function sets the shape of the Event
	 * 
	 * @param shape ({@code EventShape}) the shape of the Event
	 */
	public void setShape(EventShape shape) {
		this.shape = shape;
	}

	/**
	 * This function returns the shape of the Event
	 * 
	 * @return {@code EventShape} shape
	 */
	public EventShape getShape() {
		return shape;
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
		System.out.println("Applying effect " + e + " from " + e.getSource() + " to " + this);
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
		System.out.println(source + " invokes Event " + this);
		this.targetPos = targetPos;
	}

	@Override
	public String toString() {
		return name;
	}
}
