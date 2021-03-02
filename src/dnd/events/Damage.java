package dnd.events;

import java.util.LinkedList;

import dnd.combat.DamageDiceGroup;
import gameobjects.entities.Entity;
import maths.Vector;

/**
 * A class which represents damage dealt from a single source (though that
 * single source may deal multiple types of damage at once, such as in the case
 * of the Meteor Swarm spell).
 * 
 * @author calvi
 *
 */
public class Damage extends Event {
	protected LinkedList<DamageDiceGroup> damageDice;
	Event parent;

	/**
	 * Constructor for class Damage
	 * 
	 * @param name   ({@code String}) the name of the Damage Event
	 * @param parent ({@code Event}) the Event which created this Damage Event
	 */
	public Damage(String name, Event parent) {
		super(name, name, Event.SINGLE_TARGET);
		damageDice = new LinkedList<DamageDiceGroup>();
		this.parent = parent;
	}

	/**
	 * This function returns a deep clone of the original Damage object, such that
	 * all contained fields have unique memory addresses, but identical values to
	 * those of the original fields
	 * 
	 * @Override
	 * @return {@code Damage} clone
	 */
	public Damage clone() {
		Damage clone = new Damage(name, parent);
		for (DamageDiceGroup group : damageDice) {
			clone.addDamageDiceGroup(group.clone());
		}
		return clone;
	}

	@Override
	public void invoke(Entity source, Vector targetPos) {
		System.out.println("[JAVA] Invoking Damage " + this);

		while (source.processEvent(this, source, null))
			;
		roll();
	}

	/**
	 * This function creates a deep clone of this object, and then invokes that
	 * clone with respect to the source and target Entities provided
	 * 
	 * @param source ({@code Entity}) the Entity which created the original Damage
	 *               object
	 * @param target ({@code Entity}) the Entity which will receive the clone Damage
	 *               object
	 */
	public void invokeClone(Entity source, Entity target) {
		Damage clone = clone();
		while (source.processEvent(clone, source, target))
			;
		// Separation here to ensure sequentially prior Effects on the source
		// (such as the Dragon Sorcerer's Elemental Affinity) do not come
		// before sequentially latter Effects on the target (such as Rogue's
		// Evasion). The target shall never have a relevant sequentially
		// prior Effect in relation to the source's Effects, as the target
		// always behaves in a reactionary manner to Damage Events.
		while (target.processEvent(clone, source, target))
			;
		target.receiveDamage(clone);
	}

	/**
	 * This function adds a DamageDiceGroup object to the contained list of
	 * DamageDiceGroup objects
	 * 
	 * TODO: verify no duplicate damage types can appear in a single Damage object
	 * 
	 * @param group ({@code DamageDiceGroup}) the damage dice group to be added
	 */
	public void addDamageDiceGroup(DamageDiceGroup group) {
		damageDice.add(group);
	}

	/**
	 * This function rolls the dice contained in all of the contained
	 * DamageDiceGroup objects
	 */
	public void roll() {
		for (DamageDiceGroup group : damageDice) {
			group.roll();
		}
	}

	/**
	 * This function returns the contained list of DamageDiceGroup objects
	 * 
	 * @return {@code LinkedList<DamageDiceGroup>} damageDice
	 */
	public LinkedList<DamageDiceGroup> getDamageDice() {
		return damageDice;
	}

	/**
	 * This function returns the Event which created this Damage object
	 * 
	 * @return {@code Event} parent
	 */
	public Event getParent() {
		return parent;
	}

}
