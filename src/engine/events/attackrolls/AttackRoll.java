package engine.events.attackrolls;

import java.util.LinkedList;

import dnd.items.Item;
import dnd.items.items.Self;
import engine.events.Event;
import gameobjects.entities.Entity;

public abstract class AttackRoll extends Event {
	
	public static final Item INTRINSIC = new Self();
	
	protected Item medium;
	
	protected int attackAbilityScore;
	protected int attackBonus;
	protected int attackRoll;
	protected int numAdvantageDice;
	protected boolean isSpell;
	
	public AttackRoll(Entity source, Item medium, String name, int attackAbilityScore, boolean isSpell) {
		super(source, name);
		this.medium = medium;
		this.attackAbilityScore = attackAbilityScore;
		this.isSpell = isSpell;
	}
	
	@Override
	public void invoke(LinkedList<Entity> targets) {
		/* Attack rolls are almost always targeted at a single entity, but
		 * this structure supports multiple targets in the case that such
		 * behavior is required. All targets have individual Damage events.
		 */
		for (Entity target : targets) {
			reset();
			while (getSource().processEvent(this, target) || target.processEvent(this, target)) {
				/* Allows the effects applied to the source and the target to
				 * modify the parameters of the attack roll (e.g. Guiding
				 * Bolt grants advantage to an attack roll made against its
				 * target).
				 */
			}
			
			roll();
			
			while (getSource().processEvent(this, target) || target.processEvent(this, target)) {
				/* Allows the effects applied to the source and the target to
				 * attempt to change the outcome of the attack roll after it
				 * has been rolled (e.g. Bardic Inspiration adds a bonus to
				 * the attack roll after it has been rolled)
				 */
			}
			
			int targetAC = target.getArmorClass();
			if (attackRoll >= targetAC) {
				System.out.println("Attack roll success! (" + attackRoll + ":" + targetAC + ")");
				applyHit(target);
			}
			else {
				System.out.println("Attack roll failure! (" + attackRoll + ":" + targetAC + ")");
				applyMiss(target);
			}
		}
	}
	
	@Override
	protected void reset() {
		clearAppliedEffects();
		clearAdvantageMods();
		attackBonus = 0;
	}
	
	protected void roll() {
		d20.roll();
		attackRoll = d20.getRoll() + attackBonus;
		int tmp;
		int advantageState = getAdvantageState();
		if (advantageState == Event.HAS_DADV) {
			// disadvantage attack calculations
			System.out.println("Attacking with disadvantage!");
			d20.roll();
			tmp = d20.getRoll() + attackBonus;
			if (tmp < attackRoll) {
				attackRoll = tmp;
			}
		}
		else if (advantageState == Event.HAS_ADV) {
			// advantage attack calculations
			System.out.println("Attacking with advantage!");
			for (int i = 0; i < numAdvantageDice; i++) {
				d20.roll();
				tmp = d20.getRoll() + attackBonus;
				if (tmp > attackRoll) {
					attackRoll = tmp;
				}
			}
		}
		else if (advantageState == Event.HAS_ADV_AND_DADV) {
			System.out.println("Attacking with both advantage and disadvantage!");
		}
	}
	
	public void setNumAdvantageDice(int num) {
		numAdvantageDice = num;
	}
	
	public void setAttackAbilityScore(int score) {
		attackAbilityScore = score;
	}
	
	public int getAttackAbilityScore() {
		return attackAbilityScore;
	}
	
	public void setAttackBonus(int bonus) {
		attackBonus = bonus;
	}
	
	public int getAttackBonus() {
		return attackBonus;
	}
	
	public boolean isSpell() {
		return isSpell;
	}
	
	public int getRaw() {
		return d20.getRoll();
	}
	
	protected abstract void applyHit(Entity target);
	protected abstract void applyMiss(Entity target);
	
}
