package core.events;

import java.util.LinkedList;

import core.gameobjects.Entity;
import dnd.combat.DamageDiceGroup;
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

	public Damage(Event parent) {
		super(null, Event.SINGLE_TARGET);
		damageDice = new LinkedList<DamageDiceGroup>();
		this.parent = parent;
		name = "Damage (" + parent + ")";
	}

	public Damage clone() {
		Damage clone = new Damage(parent);
		for (DamageDiceGroup group : damageDice) {
			clone.addDamageDiceGroup(group.clone());
		}
		clone.name = name;
		return clone;
	}

	@Override
	public void invoke(Entity source, Vector targetPos) {
		System.out.println("[JAVA] " + parent + " invokes Event " + this);

		// TODO: preprocessEvent(Event e) ?
		while (source.processEvent(this, source, null))
			;
		roll();
	}

	public void invokeAsClone(Entity source, Entity target) {
		while (source.processEvent(this, source, target))
			;
		// Separation here to ensure sequentially prior Effects on the source
		// (such as the Dragon Sorcerer's Elemental Affinity) do not come
		// before sequentially latter Effects on the target (such as Rogue's
		// Evasion). The target shall never have a relevant sequentially
		// prior Effect in relation to the source's Effects, as the target
		// always behaves in a reactionary manner to Damage Events.
		while (target.processEvent(this, source, target))
			;
		target.receiveDamage(this);
	}

	public void addDamageDiceGroup(DamageDiceGroup group) {
		damageDice.add(group);
	}

	public void roll() {
		for (DamageDiceGroup group : damageDice) {
			group.roll();
		}
	}

	public LinkedList<DamageDiceGroup> getDamageDice() {
		return damageDice;
	}

	public Event getParent() {
		return parent;
	}

}
