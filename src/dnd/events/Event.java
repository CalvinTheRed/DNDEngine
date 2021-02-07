package dnd.events;

import java.util.LinkedList;

import dnd.data.EventShape;
import dnd.effects.Effect;
import gameobjects.entities.Entity;
import maths.Vector;

public abstract class Event {
	private String     name;
	private double     range;
	private EventShape shape;
	private double     radius;
	
	private LinkedList<Effect> appliedEffects;
	
	public Event(String name) {
		this.name = name;
		appliedEffects = new LinkedList<Effect>();
	}
	
	public void setRange(double range) {
		this.range = range;
	}
	
	public double getRange() {
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
	
	public boolean isEffectApplied(Effect e) {
		return appliedEffects.contains(e);
	}
	
	public void applyEffect(Effect e) {
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
