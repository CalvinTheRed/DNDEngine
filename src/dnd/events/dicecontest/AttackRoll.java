package dnd.events.dicecontest;

import dnd.data.EventShape;
import engine.Manager;
import gameobjects.entities.Entity;
import maths.Vector;

public class AttackRoll extends DiceContest {

	public AttackRoll() {
		super("Attack Roll");
		setRange(5);
		setShape(EventShape.SINGLE_TARGET);
		setRadius(0);
	}

	@Override
	public void invoke(Entity source, Vector targetPos) {
		Entity target = Manager.entityAt(targetPos);
	}
	
}
