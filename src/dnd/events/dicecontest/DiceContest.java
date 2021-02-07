package dnd.events.dicecontest;

import dnd.effects.Effect;
import dnd.events.Event;
import maths.dice.Die;

public abstract class DiceContest extends Event {
	
	public static final int DISADVANTAGE  = -1;
	public static final int NORMAL_ROLL   =  0;
	public static final int ADVANTAGE     =  1;
	public static final int CANCELLED_OUT =  2;
	
	private int advantage;
	private int disadvantage;
	private int bonus;
	
	protected Die d20;
	
	public DiceContest(String name) {
		super(name);
		d20 = new Die(20);
	}
	
	public void grantAdvantage(Effect e) {
		advantage++;
		applyEffect(e);
	}
	
	public void grantDisadvantage(Effect e) {
		disadvantage++;
		applyEffect(e);
	}
	
	protected int getAdvantageState() {
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
	
}
