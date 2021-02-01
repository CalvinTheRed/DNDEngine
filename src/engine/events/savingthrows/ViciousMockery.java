package engine.events.savingthrows;

import dnd.items.Item;
import engine.effects.Effect;
import engine.effects.ViciousMockeryEffect;
import engine.events.Damage;
import gameobjects.entities.Entity;

public class ViciousMockery extends SavingThrow {

	public ViciousMockery(Entity source, Item medium, int sourceAbilityScore) {
		super(source, medium, "Vicious Mockery", Entity.WIS, sourceAbilityScore, true);
	}

	@Override
	protected void applyPass(Entity target) {
		// No effect on a pass!
		
	}
	
	@Override
	protected void applyFail(Entity target) {
		Effect e = new ViciousMockeryEffect(getSource(), target);
		target.observeEffect(e);
	}

	@Override
	protected Damage genDamage() {
		return null;
	}

}
