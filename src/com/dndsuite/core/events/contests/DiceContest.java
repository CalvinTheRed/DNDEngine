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
	public void invokeEvent(Entity source, GameObject target) {
		if (target.hasTag(Entity.getGameObjectID())) {
			addBonus(((Entity) target).getAbilityModifier(eventAbility));
		}
		while (source.processEvent(this, source, target))
			;
		roll();
	}

	public void roll() {
		d20.roll();
		boolean hasAdvantage = hasTag(ADVANTAGE);
		boolean hasDisadvantage = hasTag(DISADVANTAGE);
		if (hasAdvantage && hasDisadvantage) {
			System.out.println("[JAVA] Rolling with both advantage and disadvantage");
		} else if (hasAdvantage) {
			System.out.println("[JAVA] Rolling with advantage");
			int roll = d20.getRoll();
			d20.roll();
			if (d20.getRoll() > roll) {
				roll = d20.getRoll();
			}
		} else if (hasDisadvantage) {
			System.out.println("[JAVA] Rolling with disadvantage");
			int roll = d20.getRoll();
			d20.roll();
			if (d20.getRoll() < roll) {
				roll = d20.getRoll();
			}
		} else {
			System.out.println("[JAVA] Rolling normally");
		}

		if (!target.hasTag(Entity.getGameObjectID())) {
			if (hasTag(AttackRoll.getEventID())) {
				// All attack rolls against game objects which have no stat block (which are not
				// Entities) are guaranteed to land.
				addTag(SET_PASS);
			} else if (hasTag(SavingThrow.getEventID())) {
				// All game objects which have no stat block (which are not Entities) are
				// guaranteed to fail any saving throws they are required to make.
				addTag(SET_FAIL);
			}
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
