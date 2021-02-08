package dnd.events.dicecontest;

import dnd.data.EventShape;
import engine.Manager;
import gameobjects.entities.Entity;
import maths.Vector;

public abstract class AttackRoll extends DiceContest {
	protected int attackAbility;
	
	public AttackRoll(String name) {
		super(name);
		// range determined by weapon used
		setShape(EventShape.SINGLE_TARGET);
		setRadius(0);
		attackAbility = Entity.STR;
	}
	
	@Override
	public void invoke(Entity source, Vector targetPos) {
		Entity target = Manager.entityAt(targetPos);
		
		while (source.processEvent(this, target) || target.processEvent(this, target));
		
		addBonus(source.getAbilityModifier(attackAbility));
		// TODO: apply proficiency bonus if appropriate
		roll();
		
		while (source.processEvent(this, target) || target.processEvent(this, target));
	}
	
	public void setAttackAbility(int attackAbility) {
		this.attackAbility = attackAbility;
	}
	
}
