package dnd.effects;

import dnd.events.Event;
import gameobjects.entities.Entity;

/**
 * A class which represents ongoing behaviors or modifiers or other effects
 * which have the ability to change the behavior of an Entity or the behavior of
 * Events invoked by or targeting Entities.
 * 
 * @author calvi
 *
 */
public abstract class Effect {

	public static final boolean HI_PRIORITY = true;
	public static final boolean LO_PRIORITY = false;

	protected Entity source;
	protected Entity target;
	protected String name;
	protected boolean ended;
	protected boolean sequencingPriority;

	/**
	 * Constructor for class Effect
	 * 
	 * @param source             (Entity) the Entity responsible for creating this
	 *                           Effect
	 * @param target             (Entity) the Entity which is inflicted with this
	 *                           Effect
	 * @param name               (String) the name of this Effect
	 * @param sequencingPriority (boolean) the priority of the Effect in an Entity's
	 *                           activeEffects list
	 */
	public Effect(Entity source, Entity target, String name, boolean sequencingPriority) {
		this.source = source;
		this.target = target;
		this.name = name;
		this.sequencingPriority = sequencingPriority;
		ended = false;
	}

	/**
	 * This function returns the Entity responsible for creating this Effect.
	 * 
	 * @return Entity
	 */
	public final Entity getSource() {
		return source;
	}

	/**
	 * This function returns the Entity which is inflicted with this Effect.
	 * 
	 * @return Entity
	 */
	public final Entity getTarget() {
		return target;
	}

	/**
	 * This function ends this Effect. An ended Effect does not have any behavior.
	 * 
	 * TODO: remove the Effect from the activeEffects list of its target upon ending
	 */
	public final void end() {
		ended = true;
	}

	/**
	 * This function returns whether this Effect is ended or active.
	 * 
	 * @return boolean
	 */
	public final boolean isEnded() {
		return ended;
	}

	/**
	 * This function returns the priority of this Effect in an Entity's
	 * activeEffects list.
	 * 
	 * @return boolean
	 */
	public boolean getSequencingPriority() {
		return sequencingPriority;
	}

	/**
	 * This function allows an Effect to change the attributes of an Event (e.g. the
	 * Dodge Effect will grant advantage to all Dex-based SavingThrow Events if the
	 * Dodge and the SavingThrow target the same Entity, among other things).
	 * 
	 * @param e      (Event) the Event being processed
	 * @param source (Entity) the source of the Event being processed
	 * @param target (Entity) the target of the Event being processed
	 * @return
	 */
	public abstract boolean processEvent(Event e, Entity source, Entity target);

	@Override
	public final String toString() {
		return name;
	}

}
