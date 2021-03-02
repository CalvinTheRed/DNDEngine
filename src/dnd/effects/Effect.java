package dnd.effects;

import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import dnd.events.Event;
import engine.Scriptable;
import gameobjects.entities.Entity;

/**
 * A class which represents ongoing behaviors or modifiers or other effects
 * which have the ability to change the behavior of an Entity or the behavior of
 * Events invoked by or targeting Entities.
 * 
 * @author calvi
 *
 */
public class Effect extends Scriptable {
	protected Entity source;
	protected Entity target;
	protected String name;
	protected boolean ended;

	/**
	 * Constructor for class Effect
	 * 
	 * @param source             ({@code Entity}) the Entity responsible for
	 *                           creating this Effect
	 * @param target             ({@code Entity}) the Entity which is inflicted with
	 *                           this Effect
	 * @param name               ({@code String}) the name of this Effect
	 * @param sequencingPriority ({@code boolean}) the priority of the Effect in an
	 *                           Entity's activeEffects list
	 */
	public Effect(String script, String name, Entity source, Entity target) {
		super(script);
		this.name = name;
		this.source = source;
		this.target = target;
		ended = false;
	}

	/**
	 * This function returns the Entity responsible for creating this Effect.
	 * 
	 * @return {@code Entity} source
	 */
	public final Entity getSource() {
		return source;
	}

	/**
	 * This function returns the Entity which is inflicted with this Effect.
	 * 
	 * @return {@code Entity} target
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
	 * @return {@code boolean} ended
	 */
	public final boolean isEnded() {
		return ended;
	}

	/**
	 * This function allows an Effect to change the attributes of an Event (e.g. the
	 * Dodge Effect will grant advantage to all Dex-based SavingThrow Events if the
	 * Dodge and the SavingThrow target the same Entity, among other things).
	 * 
	 * @param e      ({@code Event}) the Event being processed
	 * @param source ({@code Entity}) the source of the Event being processed
	 * @param target ({@code Entity}) the target of the Event being processed
	 * @return {@code boolean} did this Effect modify the Event?
	 */
	public boolean processEvent(Event e, Entity source, Entity target) {
		globals.set("event", CoerceJavaToLua.coerce(e));
		globals.set("effect", CoerceJavaToLua.coerce(this));
		globals.set("source", CoerceJavaToLua.coerce(source));
		globals.set("target", CoerceJavaToLua.coerce(target));
		globals.get("processEventSafe").invoke();
		return false;
	}

	// TODO: add processTask() abstract function?

	@Override
	public final String toString() {
		return name;
	}

}
