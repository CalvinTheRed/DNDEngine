package engine.events.savingthrows;

import java.util.LinkedList;

import dnd.items.Item;
import engine.Manager;
import engine.effects.Effect;
import engine.events.Damage;
import engine.events.Event;
import gameobjects.entities.Entity;
import maths.dice.Die;

public abstract class SavingThrow extends Event {
	protected Damage d;
	protected LinkedList<Effect> appliedEffects;
	protected Item medium;
	protected Die d20;
	protected int advantage;
	protected int disadvantage;
	protected int sourceAbilityScore;
	protected int targetAbilityScore;
	protected int saveBonus;
	protected int saveRoll;
	protected boolean isSpell;
	
	public SavingThrow(Entity source, Item medium, String name, int targetAbilityScore, int sourceAbilityScore, boolean isSpell) {
		super(source, name);
		appliedEffects = new LinkedList<Effect>();
		this.medium = medium;
		this.targetAbilityScore = targetAbilityScore;
		this.sourceAbilityScore = sourceAbilityScore;
		this.isSpell = isSpell;
	}
	
	@Override
	protected void reset() {
		d20 = new Die(20);
		appliedEffects.clear();
		advantage = 0;
		disadvantage = 0;
		sourceAbilityScore = Entity.STR;
		saveBonus = 0;
	}
	
	@Override
	public void invoke(LinkedList<Entity> targets) {
		/* Saving throw effects always share a common damage roll
		 * for their base damage, so it calculated before the
		 * targets respond to the event.
		 */
		d = genDamage();
		
		for (Entity target : targets) {
			reset();
			while (Manager.processEvent(this, target)) {
				/* Allows the Effects applied to the source and the target to
				 * modify the parameters of the saving throw (e.g. Aura of
				 * Protection grants a static bonus to saving throws).
				 */
			}
			
			roll(target);
			
			while (Manager.processEvent(this, target)) {
				/* Allows the Effects applied to the source and the target to
				 * attempt to change the outcome of the saving throw after it
				 * has been rolled (e.g. the Lucky feat allows the target to
				 * re-roll a d20 after seeing the roll of a saving throw).
				 */
			}
			
			int sourceDC = source.getSaveDiceCheck(sourceAbilityScore);
			if (saveRoll >= sourceDC) {
				System.out.println("Saving throw success! (" + saveRoll + ":" + sourceDC + ")");
				applyPass(target);
			}
			else {
				System.out.println("Saving throw failure! (" + saveRoll + ":" + sourceDC + ")");
				applyFail(target);
			}
		}
	}
	
	private void roll(Entity target) {
		d20.roll();
		saveBonus += target.getAbilityModifier(targetAbilityScore);
		saveBonus += (target.getSaveProficiency(targetAbilityScore)) ? target.getProficiencyBonus() : 0;
		saveRoll = d20.getRoll() + saveBonus;
		int tmp;
		int advantageState = getAdvantageState();
		if (advantageState == 1) {
			// advantage save calculations
			System.out.println("Saving with advantage!");
			d20.roll();
			tmp = d20.getRoll() + saveBonus;
			if (tmp > saveRoll) {
				saveRoll = tmp;
			}
		}
		else if (advantageState == -1) {
			// disadvantage save calculations
			System.out.println("Saving with disadvantage!");
			d20.roll();
			tmp = d20.getRoll() + saveBonus;
			if (tmp < saveRoll) {
				saveRoll = tmp;
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
	
	protected abstract void applyPass(Entity target);
	protected abstract void applyFail(Entity target);
	protected abstract Damage genDamage();

}
