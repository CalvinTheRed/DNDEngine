package dnd.events.dicecontests.savingthrow;

import java.util.LinkedList;

import dnd.events.dicecontests.DiceContest;
import gameobjects.entities.Entity;

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
	public SavingThrow(String name, double shortrange, double longrange, double radius, int dcAbility,
			int saveAbility) {
		super(name);
		targets = new LinkedList<Entity>();
		passedTargets = new LinkedList<Entity>();
		failedTargets = new LinkedList<Entity>();
		this.shortrange = shortrange;
		this.longrange = longrange;
		this.radius = radius;
		this.dcAbility = dcAbility;
		this.saveAbility = saveAbility;
	}

	/**
	 * This function returns the ability score which is used when the source of the
	 * saving throw calculates the save DC.
	 * 
	 * @return {@code int} dc ability index [0,5]
	 */
	public int getDCAbility() {
		return dcAbility;
	}

	/**
	 * This function returns the ability score the targets of the saving throw will
	 * use when attempting to resist the effects of the saving throw.
	 * 
	 * @return {@code int} save ability index [0,5]
	 */
	public int getSaveAbility() {
		return saveAbility;
	}

	/**
	 * This function returns a list of the target Entity objects which have passed
	 * their saving throws against this event.
	 * 
	 * @return {@code LinkedList<Entity>} passedTargets
	 */
	public LinkedList<Entity> getPassedTargets() {
		return passedTargets;
	}

	/**
	 * This function returns a list of the target Entity objects which have failed
	 * their saving throws against this event.
	 * 
	 * @return {@code LinkedList<Entity>} failedTargets
	 */
	public LinkedList<Entity> getFailedTargets() {
		return failedTargets;
	}

	/**
	 * This function provides a deep clone of this event. All fields are cloned to
	 * new memory addresses where appropriate.
	 * 
	 * @return {@code SavingThrow} clone
	 */
	public abstract SavingThrow clone();

}
