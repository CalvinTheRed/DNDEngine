package dnd.events;

import java.util.LinkedList;

import dnd.combat.DamageDiceGroup;
import engine.Manager;
import gameobjects.entities.Entity;
import maths.Vector;

public class Damage extends Event {
	protected LinkedList<DamageDiceGroup> damageDice;
	Event parent;
	
	public Damage(String name, Event parent) {
		super(name);
		damageDice = new LinkedList<DamageDiceGroup>();
		this.parent = parent;
	}

	@Override
	public void invoke(Entity source, Vector targetPos) {
		Entity target = Manager.entityAt(targetPos);
		
		while (source.processEvent(this, source, target) || target.processEvent(this, source, target));
		roll();
		while (source.processEvent(this, source, target) || target.processEvent(this, source, target));
		
		source.receiveDamage(this);
	}
	
	public Event getParent() {
		return parent;
	}
	
	public void addDamageDiceGroup(DamageDiceGroup group) {
		damageDice.add(group);
	}
	
	protected void roll() {
		for (DamageDiceGroup group : damageDice) {
			group.roll();
		}
	}

}
