package dnd.events.dicecontests.attackroll;

import dnd.data.EventShape;
import dnd.events.dicecontests.DiceContest;
import engine.VirtualBoard;
import gameobjects.entities.Entity;
import maths.Vector;

/**
 * A class representing all Events which involve an attack roll such as weapon
 * attacks or ranged or melee spell attacks.
 * 
 * @author calvi
 *
 */
public abstract class AttackRoll extends DiceContest {
	protected int attackAbility;
	protected Entity target;

	/**
	 * Constructor for class AttackRoll
	 * 
	 * @param name          (String) the name of the AttackRoll
	 * @param attackAbility (int) the index of the ability score being used to make
	 *                      the attack roll
	 */
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

		while (source.processEvent(this, source, target) || target.processEvent(this, source, target))
			;
		roll();
		while (source.processEvent(this, source, target) || target.processEvent(this, source, target))
			;

		// TODO: make an attack roll check properly here
		invokeFallout(source);
	}

}
