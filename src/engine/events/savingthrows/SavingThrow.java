package engine.events.savingthrows;

import java.util.LinkedList;

import dnd.items.Item;
import engine.events.Damage;
import engine.events.Event;
import gameobjects.entities.Entity;

public abstract class SavingThrow extends Event {
	protected Damage d;
	
	protected Item medium;
	protected int sourceAbilityScore;
	protected int targetAbilityScore;
	protected int saveBonus;
	protected int saveRoll;
	protected boolean isSpell;
	
	public SavingThrow(Entity source, Item medium, String name, int targetAbilityScore, int sourceAbilityScore, boolean isSpell) {
		super(source, name);
		this.medium = medium;
		this.targetAbilityScore = targetAbilityScore;
		this.sourceAbilityScore = sourceAbilityScore;
		this.isSpell = isSpell;
	}
	
	@Override
	public void invoke(LinkedList<Entity> targets) {
		/* Saving throw effects always share a common damage roll
		 * for their base damage, so it calculated before the
		 * targets respond to the event.
		 */
		d = genDamage();
		d.invoke(null);
		
		for (Entity target : targets) {
			reset();
			while (getSource().processEvent(this, target) || target.processEvent(this,  target)) {
				/* Allows the Effects applied to the source and the target to
				 * modify the parameters of the saving throw (e.g. Aura of
				 * Protection grants a static bonus to saving throws).
				 */
			}
			
			roll(target);
			
			while (getSource().processEvent(this, target) || target.processEvent(this, target)) {
				/* Allows the Effects applied to the source and the target to
				 * attempt to change the outcome of the saving throw after it
				 * has been rolled (e.g. the Lucky feat allows the target to
				 * re-roll a d20 after seeing the roll of a saving throw).
				 */
			}
			
			int sourceDC = getSource().getSaveDiceCheck(sourceAbilityScore);
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
	
	@Override
	protected void reset() {
		clearAppliedEffects();
		clearAdvantageMods();
		saveBonus = 0;
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
