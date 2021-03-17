package com.dndsuite.core.events;

import java.util.LinkedList;

import com.dndsuite.core.events.contests.AttackRoll;
import com.dndsuite.core.gameobjects.Entity;
import com.dndsuite.dnd.combat.DamageDiceGroup;
import com.dndsuite.maths.Vector;
import com.dndsuite.maths.dice.Die;

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
	protected Event parent;

	public Damage(Event parent) {
		super(null);
		damageDice = new LinkedList<DamageDiceGroup>();
		this.parent = parent;
		name = "Damage (" + parent + ")";
		addTag(Event.SINGLE_TARGET);
		addTag(Damage.getEventID());
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
		System.out.println("[JAVA] " + parent + " invokes event " + this);

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

	public void invokeHalvedAsClone(Entity source, Entity target) {
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

		for (DamageDiceGroup group : damageDice) {
			group.halve();
		}
		target.receiveDamage(this);
	}

	public void addDamageDiceGroup(DamageDiceGroup newGroup) {
		for (DamageDiceGroup group : damageDice) {
			if (group.getDamageType() == newGroup.getDamageType()) {
				for (Die d : newGroup.getDice()) {
					group.addDie(d);
					group.addBonus(newGroup.getBonus());
					return;
				}
			}
		}
		damageDice.add(newGroup);
	}

	public void roll() {
		if (hasTag(AttackRoll.CRITICAL_HIT)) {
			System.out.println("[JAVA] CRITICAL HIT!!!");
			LinkedList<DamageDiceGroup> criticalDice = new LinkedList<DamageDiceGroup>();
			for (DamageDiceGroup group : damageDice) {
				DamageDiceGroup criticalClone = group.clone();
				criticalClone.addBonus(-criticalClone.getBonus());
				criticalDice.add(criticalClone);
			}
			for (DamageDiceGroup group : criticalDice) {
				addDamageDiceGroup(group);
			}
		}

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

	public static String getEventID() {
		return "Damage";
	}

}
