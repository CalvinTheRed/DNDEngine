package dnd.events.dicecontests;

import dnd.data.EventShape;
import gameobjects.entities.Entity;

public abstract class AttackRoll extends DiceContest {
	protected int attackAbility;
	protected Entity target;
	
	public AttackRoll(String name, int attackAbility) {
		super(name);
		setShape(EventShape.SINGLE_TARGET);
		setRadius(0);
		this.attackAbility = attackAbility;
	}
	
}
