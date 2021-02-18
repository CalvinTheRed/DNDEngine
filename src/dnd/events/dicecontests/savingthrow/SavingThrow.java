package dnd.events.dicecontests.savingthrow;

import java.util.LinkedList;

import dnd.data.EventShape;
import dnd.events.dicecontests.DiceContest;
import engine.VirtualBoard;
import gameobjects.entities.Entity;
import maths.Vector;

/**
 * A class representing all forms of saving throw effects such as spells, breath
 * weapons, certain traps, etc.
 * 
 * @author calvi
 *
 */
public abstract class SavingThrow extends DiceContest {
	protected int dcAbility;
	protected int saveAbility;
	protected LinkedList<Entity> targets;
	protected LinkedList<Entity> passedTargets;
	protected LinkedList<Entity> failedTargets;

	/**
	 * Constructor for class SavingThrow
	 * 
	 * @param name        ({@code String}) the name of saving throw
	 * @param shape       ({@code EventShape}) the shape of the saving throw's area
	 *                    of effect
	 * @param range       ({@code double[2]}) the long and short range of the area
	 *                    effect - normally these will be the same for saving-throw
	 *                    based effects
	 * @param radius      (double) the radius of the event shape, if applicable
	 * @param dcAbility   ({@code int}) the ability score which is used when the
	 *                    source of the saving throw calculates the save DC
	 * @param saveAbility ({@code int}) the ability score the targets of the saving
	 *                    throw will use when attempting to resist the effects of
	 *                    the saving throw
	 */
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
	public void invoke(Entity source, Vector targetPos) {
		super.invoke(source, targetPos);
		if (shape == EventShape.CONE) {
			targets.addAll(VirtualBoard.entitiesInCone2(targetPos, radius, source.getRot()));
		} else if (shape == EventShape.CUBE) {
			targets.addAll(VirtualBoard.entitiesInCube(targetPos, source.getRot(), radius));
		} else if (shape == EventShape.LINE) {
			targets.addAll(VirtualBoard.entitiesInLine(targetPos, source.getRot(), range[LONGRANGE], radius));
		} else if (shape == EventShape.SINGLE_TARGET) {
			targets.add(VirtualBoard.entityAt(targetPos));
		} else if (shape == EventShape.SPHERE) {
			targets.addAll(VirtualBoard.entitiesInSphere(targetPos, radius));
		} else {
			System.out.println("ERR: SavingThrow shape not defined");
		}

		// Source must apply all relevant modifications prior to cloning & rolling
		// This processing is independent of the target
		while (source.processEvent(this, source, null))
			;

		for (Entity target : targets) {
			SavingThrow clone = clone();

			while (source.processEvent(clone, source, target) || target.processEvent(clone, source, target))
				;
			clone.roll();
			while (source.processEvent(clone, source, target) || target.processEvent(clone, source, target))
				;
			failedTargets.add(target);
		}
		invokeFallout(source);
	}

	/**
	 * This function returns the ability score which is used when the source of the
	 * saving throw calculates the save DC.
	 * 
	 * @return {@code int}
	 */
	public int getDCAbility() {
		return dcAbility;
	}

	/**
	 * This function returns the ability score the targets of the saving throw will
	 * use when attempting to resist the effects of the saving throw.
	 * 
	 * @return
	 */
	public int getSaveAbility() {
		return saveAbility;
	}

	/**
	 * This function returns a list of the target Entity objects which have passed
	 * their saving throws against this saving throw.
	 * 
	 * @return {@code LinkedList<Entity>}
	 */
	public LinkedList<Entity> getPassedTargets() {
		return passedTargets;
	}

	public LinkedList<Entity> getFailedTargets() {
		return failedTargets;
	}

	public abstract SavingThrow clone();

}
