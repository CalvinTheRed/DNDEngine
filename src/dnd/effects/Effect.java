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
	
	public final Entity getSource() {
		return source;
	}
	
	public final Entity getTarget() {
		return target;
	}
	
	public final void end() {
		ended = true;
	}
	
	public final boolean isEnded() {
		return ended;
	}
	
	public abstract boolean processEvent(Event e, Entity source, Entity target);
	
	@Override
	public final String toString() {
		return name;
	}
	
}
