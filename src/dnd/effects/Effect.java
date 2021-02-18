package dnd.effects;

import dnd.events.Event;
import gameobjects.entities.Entity;

public abstract class Effect {
	
	public static final boolean HI_PRIORITY = true;
	public static final boolean LO_PRIORITY = false;
	
	protected Entity source;
	protected Entity target;
	protected String name;
	protected boolean ended;
	protected boolean sequencingPriority;
	
	public Effect(Entity source, Entity target, String name, boolean sequencingPriority) {
		this.source = source;
		this.target = target;
		this.name = name;
		this.sequencingPriority = sequencingPriority;
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
	
	public boolean getSequencingPriority() {
		return sequencingPriority;
	}
	
	public abstract boolean processEvent(Event e, Entity source, Entity target);
	
	@Override
	public final String toString() {
		return name;
	}
	
}
