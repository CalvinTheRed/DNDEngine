package dnd.effects;

import dnd.events.Event;
import gameobjects.entities.Entity;

public abstract class Effect {
	private Entity source;
	private Entity target;
	private String name;
	private boolean ended;
	
	public Effect(Entity source, Entity target, String name) {
		this.source = source;
		this.target = target;
		this.name = name;
		ended = false;
	}
	
	public Entity getSource() {
		return source;
	}
	
	public Entity getTarget() {
		return target;
	}
	
	public void end() {
		ended = true;
	}
	
	public boolean isEnded() {
		return ended;
	}
	
	public abstract boolean processEvent(Event e, Entity target);
	
	@Override
	public String toString() {
		return name;
	}
	
}
