package dnd.events.dicecontests;

import dnd.effects.Effect;
import dnd.events.Event;
import gameobjects.entities.Entity;
import maths.dice.Die;

public abstract class DiceContest extends Event {
	
	public static final int DISADVANTAGE  = -1;
	public static final int NORMAL_ROLL   =  0;
	public static final int ADVANTAGE     =  1;
	public static final int CANCELLED_OUT =  2;
	
	protected int advantage;
	protected int disadvantage;
	protected int bonus;
	protected Die d20;
	
	public DiceContest(String name) {
		super(name);
		d20 = new Die(20);
	}
	
	public void grantAdvantage(Effect e) throws Exception {
		advantage++;
	}
	
	public void grantDisadvantage(Effect e) throws Exception {
		disadvantage++;
	}
	
	private int getAdvantageState() {
		if (advantage > 0 && disadvantage > 0) {
			return CANCELLED_OUT;
		}
		if (advantage > 0) {
			return ADVANTAGE;
		}
		if (disadvantage > 0) {
			return DISADVANTAGE;
		}
		return NORMAL_ROLL;
	}
	
	public void addBonus(int bonus) {
		this.bonus += bonus;
	}
	
	public int getBonus() {
		return bonus;
	}
	
	public int getRawRoll() {
		return d20.getRoll();
	}
	
	protected void roll() {
		d20.roll();
		int advantageState = getAdvantageState();
		if (advantageState == ADVANTAGE) {
			System.out.println("Rolling with advantage!");
			int roll = d20.getRoll();
			d20.roll();
			if (d20.getRoll() > roll) {
				roll = d20.getRoll();
			}
		}
		else if (advantageState == DISADVANTAGE) {
			System.out.println("Rolling with disadvantage!");
			int roll = d20.getRoll();
			d20.roll();
			if (d20.getRoll() < roll) {
				roll = d20.getRoll();
			}
		}
		else if (advantageState == NORMAL_ROLL) {
			System.out.println("Rolling normally!");
		}
		else {
			System.out.println("Rolling with both advantage and disadvantage!");
		}
	}
	
	protected abstract void invokeFallout(Entity source);
	
}
