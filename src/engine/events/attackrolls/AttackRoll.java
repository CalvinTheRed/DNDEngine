package engine.events.attackrolls;

import java.util.LinkedList;

import dnd.items.Item;
import dnd.items.items.Self;
import engine.Manager;
import engine.effects.Effect;
import engine.events.Event;
import gameobjects.entities.Entity;
import maths.dice.Die;

public abstract class AttackRoll extends Event {
	
	public static final Item INTRINSIC = new Self();
	
	protected LinkedList<Effect> appliedEffects;
	protected Item medium;
	protected Die d20;
	protected int advantage;
	protected int disadvantage;
	protected int attackAbilityScore;
	protected int attackBonus;
	protected int attackRoll;
	protected int numAdvantageDice;
	protected boolean isSpell;
	
	public AttackRoll(Entity source, Item medium, String name, int attackAbilityScore, boolean isSpell) {
		super(source, name);
		appliedEffects = new LinkedList<Effect>();
		this.medium = medium;
		this.attackAbilityScore = attackAbilityScore;
		this.isSpell = isSpell;
	}
	
	@Override
	protected void reset() {
		d20 = new Die(20);
		appliedEffects.clear();
		advantage = 0;
		disadvantage = 0;
		attackAbilityScore = Entity.STR;
		attackBonus = source.getAbilityModifier(Entity.STR);
		attackBonus += (isSpell || medium == INTRINSIC || source.hasWeaponProficiency(medium.getWeaponType()) ? source.getProficiencyBonus() : 0);
		numAdvantageDice = 2;
	}
	
	@Override
	public void invoke(LinkedList<Entity> targets) {
		/* Attack rolls are almost always targeted at a single entity, but
		 * this structure supports multiple targets in the case that such
		 * behavior is required.
		 */
		for (Entity target : targets) {
			reset();
			while (Manager.processEvent(this, target)) {
				/* Allows the effects applied to the source and the target to
				 * modify the parameters of the attack roll (e.g. Guiding
				 * Bolt grants advantage to an attack roll made against its
				 * target).
				 */
			}
			
			roll();
			
			while (Manager.processEvent(this, target)) {
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
	
	protected void roll() {
		d20.roll();
		attackRoll = d20.getRoll() + attackBonus;
		int tmp;
		int advantageState = getAdvantageState();
		if (advantageState == 1) {
			// advantage attack calculations
			System.out.println("Attacking with advantage!");
			for (int i = 1; i < numAdvantageDice; i++) {
				d20.roll();
				tmp = d20.getRoll() + attackBonus;
				if (tmp > attackRoll) {
					attackRoll = tmp;
				}
			}
		}
		else if (advantageState == -1) {
			// disadvantage attack calculations
			System.out.println("Attacking with disadvantage!");
			d20.roll();
			tmp = d20.getRoll() + attackBonus;
			if (tmp < attackRoll) {
				attackRoll = tmp;
			}
		}
	}
	
	public void grantAdvantage() {
		advantage++;
	}
	
	public void grantDisadvantage() {
		disadvantage++;
	}
	
	private int getAdvantageState() {
		if (advantage > 0 && disadvantage > 0) {
			return 0;
		}
		if (advantage > 0) {
			return 1;
		}
		if (disadvantage > 0) {
			return -1;
		}
		return 0;
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
	
	public boolean isEffectApplied(Effect e) {
		return appliedEffects.contains(e);
	}
	
	public void applyEffect(Effect e) {
		appliedEffects.add(e);
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
