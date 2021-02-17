package dnd.events;

import java.util.LinkedList;

import dnd.data.EventShape;
import dnd.effects.Effect;
import gameobjects.entities.Entity;
import maths.Vector;

public abstract class Event {
	
	public static final int SHORTRANGE = 0;
	public static final int LONGRANGE  = 1;
	
	protected LinkedList<Effect> appliedEffects;
	protected Vector     targetPos;
	protected String     name;
	protected double[]   range;
	protected EventShape shape;
	protected double     radius;
	
	public Event(String name) {
		this.name = name;
		appliedEffects = new LinkedList<Effect>();
		range = new double[2];
	}
	
	public void setRange(double shortrange, double longrange) {
		range[SHORTRANGE] = shortrange;
		range[LONGRANGE] = longrange;
	}
	
	public double[] getRange() {
		return range;
	}
	
	public void setShape(EventShape shape) {
		this.shape = shape;
	}
	
	public EventShape getShape() {
		return shape;
	}
	
	public void setRadius(int radius) {
		this.radius = radius;
	}
	
	public double getRadius() {
		return radius;
	}
	
	public void applyEffect(Effect e) throws Exception {
		if (appliedEffects.contains(e)) {
			throw new Exception("Effect already applied");
		}
		System.out.println("Applying effect " + e + " to " + this);
		appliedEffects.add(e);
	}
	
	public void clearAppliedEffects() {
		appliedEffects.clear();
	}
	
	public void invoke(Entity source, Vector targetPos) {
		System.out.println(source + " invokes Event " + this);
		this.targetPos = targetPos;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
