package engine.events;

import java.util.LinkedList;

import dnd.combat.DamageDiceGroup;
import gameobjects.entities.Entity;

/*
 * This class is not meant to represent damage being dealt.
 * Rather, it represents the value of potential damage for
 * the purposes of damage calculations. The Damage object
 * will then be passed to its target, which will take damage
 * according to its resistances, immunities, and vulnerabilities,
 * and will have the option of reacting at that point.
 */

public class Damage extends Event {
	protected LinkedList<DamageDiceGroup> damageDice;
	
	public Damage(Entity source, String sourceEventName) {
		super(source, "Damage from " + sourceEventName);
	}

	@Override
	public void invoke(LinkedList<Entity> targets) {
		for (DamageDiceGroup group : damageDice){
			group.roll();
		}
	}
	
	public LinkedList<DamageDiceGroup> getDamageDice(){
		return damageDice;
	}
	
	public void setDamageDice(LinkedList<DamageDiceGroup> damageDice) {
		this.damageDice = damageDice;
	}

	@Override
	protected void reset() {
		// TODO Auto-generated method stub
		
	}
	
}
