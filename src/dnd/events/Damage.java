package dnd.events;

import java.util.LinkedList;

import dnd.combat.DamageDiceGroup;
import gameobjects.entities.Entity;
import maths.Vector;

public class Damage extends Event {
	protected LinkedList<DamageDiceGroup> damageDice;
	protected Event parent;
	
	public Damage(String name, Event parent) {
		super(name);
		damageDice = new LinkedList<DamageDiceGroup>();
		this.parent = parent;
		cloneAreaParams(parent);
	}
	
	@Override
	public Damage clone() {
		Damage d = new Damage(name, parent);
		for (DamageDiceGroup group : damageDice) {
			d.addDamageDiceGroup(group.clone());
		}
		return d;
	}

	@Override
	public void invoke(Entity source, Vector targetPos) {
		System.out.println("Invoking Damage " + this);
		
		while (source.processEvent(this, source, null));
		roll();
	}
	
	public void invokeClone(Entity source, Entity target) {
		Damage clone = clone();
		while (source.processEvent(clone, source, target) || target.processEvent(clone, source, target));
		source.receiveDamage(clone);
	}
	
	protected void cloneAreaParams(Event other) {
		range = other.range.clone();
		shape = other.shape;
		radius = other.radius;
	}
	
	public Event getParent() {
		return parent;
	}
	
	public void addDamageDiceGroup(DamageDiceGroup group) {
		damageDice.add(group);
	}
	
	public void roll() {
		for (DamageDiceGroup group : damageDice) {
			group.roll();
		}
	}
	
	public LinkedList<DamageDiceGroup> getDamageDice(){
		return damageDice;
	}

}
