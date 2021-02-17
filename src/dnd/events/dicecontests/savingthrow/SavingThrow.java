package dnd.events.dicecontests.savingthrow;

import java.util.LinkedList;

import dnd.data.EventShape;
import dnd.effects.Effect;
import dnd.events.dicecontests.DiceContest;
import engine.VirtualBoard;
import gameobjects.entities.Entity;
import maths.Vector;

public class SavingThrow extends DiceContest {
	protected int dcAbility;
	protected int saveAbility;
	protected LinkedList<Entity> targets;
	protected LinkedList<Entity> passedTargets;
	protected LinkedList<Entity> failedTargets;
	
	public SavingThrow(String name, EventShape shape, double[] range, double radius, int dcAbility, int saveAbility) {
		super(name);
		targets = new LinkedList<Entity>();
		passedTargets = new LinkedList<Entity>();
		failedTargets = new LinkedList<Entity>();
		this.shape = shape;
		this.range[SHORTRANGE] = range[SHORTRANGE];
		this.range[LONGRANGE] = range[LONGRANGE];
		this.radius = radius;
		this.dcAbility = dcAbility;
		this.saveAbility = saveAbility;
	}
	
	@Override
	public SavingThrow clone() {
		SavingThrow clone = new SavingThrow(name, shape, range, radius, dcAbility, saveAbility);
		clone.advantage = advantage;
		clone.disadvantage = disadvantage;
		clone.bonus = bonus;
		for (Effect e : appliedEffects) {
			clone.appliedEffects.add(e);
		}
		clone.d20 = d20.clone();
		return clone;
	}
	
	@Override
	public void invoke(Entity source, Vector targetPos) {
		super.invoke(source, targetPos);
		if (shape == EventShape.CONE) {
			targets.addAll(VirtualBoard.entitiesInCone2(targetPos, radius, source.getRot()));
		}
		else if (shape == EventShape.CUBE) {
			targets.addAll(VirtualBoard.entitiesInCube(targetPos, source.getRot(), radius));
		}
		else if (shape == EventShape.LINE) {
			targets.addAll(VirtualBoard.entitiesInLine(targetPos, source.getRot(), range[LONGRANGE], radius));
		}
		else if (shape == EventShape.SINGLE_TARGET) {
			targets.add(VirtualBoard.entityAt(targetPos));
		}
		else if (shape == EventShape.SPHERE) {
			targets.addAll(VirtualBoard.entitiesInSphere(targetPos, radius));
		}
		else {
			System.out.println("ERR: SavingThrow shape not defined");
		}
		
		// Source must apply all relevant modifications prior to cloning & rolling
		// This processing is independent of the target
		while (source.processEvent(this, source, null));
		
		for (Entity target : targets) {
			SavingThrow clone = clone();
			
			while (source.processEvent(clone, source, target) || target.processEvent(clone, source, target));
			clone.roll();
			while (source.processEvent(clone, source, target) || target.processEvent(clone, source, target));
			
			// TODO: make a saving throw check properly here
			failedTargets.add(target);
		}
		invokeFallout(source);
	}

	@Override
	protected void invokeFallout(Entity source) {
	}
	
	public int getDCAbility() {
		return dcAbility;
	}
	
	public int getSaveAbility() {
		return saveAbility;
	}
	
}
