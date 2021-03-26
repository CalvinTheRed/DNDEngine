package com.dndsuite.core.events.contests;

import java.util.LinkedList;

import com.dndsuite.core.events.Event;
import com.dndsuite.core.gameobjects.Entity;
import com.dndsuite.core.gameobjects.GameObject;
import com.dndsuite.dnd.VirtualBoard;
import com.dndsuite.maths.Vector;
import com.dndsuite.maths.dice.Die;

public abstract class DiceContest extends Event {
	public static final String ADVANTAGE = "Advantage";
	public static final String DISADVANTAGE = "Disadvantage";
	public static final String SET_PASS = "Set Pass";
	public static final String SET_FAIL = "Set Fail";

	protected Event parent;
	protected GameObject target;
	protected Die d20;
	protected int bonus;

	public DiceContest(String script, int contestAbility, Event parent, GameObject target) {
		super(script, contestAbility);
		this.parent = parent;
		this.target = target;
		d20 = new Die(20);
		setRange(0.0, 0.0);
		setRadius(0.0);
		addTag(DiceContest.getEventID());
	}

	public void addBonus(int bonus) {
		this.bonus += bonus;
	}

	@Override
	public DiceContest clone() {
		DiceContest clone = (DiceContest) super.clone(); // TODO <------- this is a problem, casting does not work in
														 // this direction
		clone.bonus = bonus;
		clone.parent = parent;
		clone.target = target;
		clone.d20 = d20.clone();
		return clone;
	}

	public int getBonus() {
		return bonus;
	}

	public Event getParent() {
		return parent;
	}

	public int getRawRoll() {
		return d20.getRoll();
	}

	public int getRoll() {
		return d20.getRoll() + bonus;
	}

	@Override
	protected void invokeEvent(Entity source, GameObject target) {
		while (source.processEvent(this, source, target))
			;
		roll();
	}

	protected void roll() {
		d20.roll();
		boolean hasAdvantage = hasTag(ADVANTAGE);
		boolean hasDisadvantage = hasTag(DISADVANTAGE);
		if (hasAdvantage && hasDisadvantage) {
			System.out.println("[JAVA] Rolling with both advantage and disadvantage!");
		} else if (hasAdvantage) {
			System.out.println("[JAVA] Rolling with advantage!");
			int roll = d20.getRoll();
			d20.roll();
			if (d20.getRoll() > roll) {
				roll = d20.getRoll();
			}
		} else if (hasDisadvantage) {
			System.out.println("[JAVA] Rolling with disadvantage!");
			int roll = d20.getRoll();
			d20.roll();
			if (d20.getRoll() < roll) {
				roll = d20.getRoll();
			}
		} else {
			System.out.println("[JAVA] Rolling normally!");
		}

		if (hasTag(AttackRoll.getEventID()) && !target.hasTag(Entity.getGameObjectID())) {
			// All attack rolls against game objects which have no stat block (which are not
			// Entities) are guaranteed to land.
			addTag(SET_PASS);
		}

		if (hasTag(SavingThrow.getEventID()) && !target.hasTag(Entity.getGameObjectID())) {
			// All game objects which have no stat block (which are not Entities) are
			// guaranteed to fail any saving throws they are required to make.
			addTag(SET_FAIL);
		}
	}

	@Override
	protected LinkedList<GameObject> targets(Vector targetPos) {
		LinkedList<GameObject> targets = new LinkedList<GameObject>();
		targets.add(VirtualBoard.nearestObject(targetPos, new String[] {}));
		return targets;
	}

	public static String getEventID() {
		return "Dice Contest";
	}

}
