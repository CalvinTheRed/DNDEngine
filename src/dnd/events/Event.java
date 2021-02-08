package dnd.events;

import java.util.LinkedList;

import dnd.data.EventShape;
import dnd.effects.Effect;
import gameobjects.entities.Entity;
import maths.Vector;

public abstract class Event {
	
	public static final int SHORTRANGE = 0;
	public static final int LONGRANGE  = 1;
	
	private String     name;
	private double[]   range;
	private EventShape shape;
	private double     radius;
	
	private LinkedList<Effect> appliedEffects;
	
	public Event(String name) {
		this.name = name;
		appliedEffects = new LinkedList<Effect>();
		range = new double[2];
	}
	
	public void setRange(double shortrange, double longrange) {
		range[0] = shortrange;
		range[1] = longrange;
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
		appliedEffects.add(e);
	}
	
	public void clearAppliedEffects() {
		appliedEffects.clear();
	}
	
	public abstract void invoke(Entity source, Vector targetPos);
	
	@Override
	public String toString() {
		return name;
	}
}
