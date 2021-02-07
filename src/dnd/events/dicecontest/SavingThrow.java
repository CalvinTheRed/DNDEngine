package dnd.events.dicecontest;

import java.util.LinkedList;

import engine.Manager;
import gameobjects.entities.Entity;
import maths.Vector;

public class SavingThrow extends DiceContest {

	public SavingThrow(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void invoke(Entity source, Vector targetPos) {
		LinkedList<Entity> targets = new LinkedList<Entity>();
		
		switch (getShape()) {
		case CONE:
			targets.addAll(Manager.entitiesInCone(source.getPos(), getRadius(), source.getRot()));
		case CUBE:
			targets.addAll(Manager.entitiesInCube(targetPos, source.getRot(), getRadius()));
		case LINE:
			targets.addAll(Manager.entitiesInLine(source.getPos(), source.getRot(), getRange(), getRadius()));
		case SINGLE_TARGET:
			targets.add(Manager.entityAt(targetPos));
		case SPHERE:
			targets.addAll(Manager.entitiesInSphere(targetPos, getRadius()));
		default:
			System.out.println("ERROR: Event shape did not match any known geometries!");
		}
		
	}

}
