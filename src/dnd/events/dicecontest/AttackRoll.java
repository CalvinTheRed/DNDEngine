package dnd.events.dicecontest;

import dnd.data.EventShape;
import gameobjects.entities.Entity;

public abstract class AttackRoll extends DiceContest {
	protected int attackAbility;
	
	public AttackRoll(String name) {
		super(name);
		// range determined by weapon used
		setShape(EventShape.SINGLE_TARGET);
		setRadius(0);
		attackAbility = Entity.STR;
	}
	
	public void setAttackAbility(int attackAbility) {
		this.attackAbility = attackAbility;
	}
	
}
