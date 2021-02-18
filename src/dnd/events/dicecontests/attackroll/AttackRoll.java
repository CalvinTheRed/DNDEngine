package dnd.events.dicecontests.attackroll;

import dnd.data.EventShape;
import dnd.events.dicecontests.DiceContest;
import engine.VirtualBoard;
import gameobjects.entities.Entity;
import maths.Vector;

public abstract class AttackRoll extends DiceContest {
	protected int attackAbility;
	protected Entity target;
	
	public AttackRoll(String name, int attackAbility) {
		super(name);
		setShape(EventShape.SINGLE_TARGET);
		setRadius(0);
		this.attackAbility = attackAbility;
	}
	
	@Override
	public void invoke(Entity source, Vector targetPos) {
		super.invoke(source, targetPos);
		target = VirtualBoard.entityAt(targetPos);
		
		while (source.processEvent(this, source, target) || target.processEvent(this, source, target));
		roll();
		while (source.processEvent(this, source, target) || target.processEvent(this, source, target));
		
		// TODO: make an attack roll check properly here
		invokeFallout(source);
	}
	
}
