package engine.events.savingthrows;

import java.util.LinkedList;

import dnd.combat.DamageDiceGroup;
import dnd.data.DamageType;
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
		
		target.processDamageEvent(d);
	}

	@Override
	protected Damage genDamage() {
		Damage d = new Damage(getSource(), toString());
		LinkedList<DamageDiceGroup> damageDice = new LinkedList<DamageDiceGroup>();
		damageDice.add(new DamageDiceGroup(1, 4, DamageType.PSYCHIC));
		d.setDamageDice(damageDice);
		return d;
	}

}
