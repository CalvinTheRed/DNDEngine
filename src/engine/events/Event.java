package engine.events;

import java.util.LinkedList;

import gameobjects.entities.Entity;

public abstract class Event {
	private Entity source;
	private String name;
	private LinkedList<Entity> targets;
	
	public Event(Entity source, String name) {
		this.source = source;
		this.name = name;
		targets = new LinkedList<Entity>();
	}
	
	public Entity getSource() {
		return source;
	}
	
	public LinkedList<Entity> getTargets(){
		return targets;
	}
	
	public abstract void invoke(LinkedList<Entity> targets);
	
	@Override
	public String toString() {
		return name;
	}
}
