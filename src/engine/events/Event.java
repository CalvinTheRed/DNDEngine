package engine.events;

import java.util.LinkedList;

import gameobjects.entities.Entity;

public abstract class Event {
	protected Entity source;
	protected String name;
	
	public Event(Entity source, String name) {
		this.source = source;
		this.name = name;
	}
	
	public Entity getSource() {
		return source;
	}
	
	public abstract void invoke(LinkedList<Entity> targets);
	protected abstract void reset();
	
	@Override
	public String toString() {
		return name;
	}
}
