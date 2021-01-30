package engine.effects;

import engine.events.Event;
import gameobjects.entities.Entity;

public abstract class Effect {
	protected Entity source;
	protected Entity target;
	protected String name;
	
	public Effect(Entity source, Entity target, String name) {
		this.source = source;
		this.target = target;
		this.name = name;
	}
	
	public Entity getSource() {
		return source;
	}
	
	public Entity getTarget() {
		return target;
	}
	
	public abstract void expire();
	public abstract boolean processEvent(Event e, Entity target);
	
	@Override
	public String toString() {
		return name;
	}
	
}
