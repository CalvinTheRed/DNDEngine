package engine.events.savingthrows;

import java.util.LinkedList;

import dnd.combat.DamageDiceGroup;
import dnd.data.DamageType;
import dnd.items.Item;
import engine.Manager;
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
		target.processDamageEvent(d);
		Manager.addEffect(new ViciousMockeryEffect(source, target));
	}

	@Override
	protected Damage genDamage() {
		Damage d = new Damage(source, null, name);
		LinkedList<DamageDiceGroup> damageDice = new LinkedList<DamageDiceGroup>();
		
		int numDice;
		if (source.getLevel() >= 17) {
			numDice = 4;
		}
		else if (source.getLevel() >= 11) {
			numDice = 3;
		}
		else if (source.getLevel() >= 5) {
			numDice = 2;
		}
		else {
			numDice = 1;
		}
		
		damageDice.add(new DamageDiceGroup(numDice, 4, DamageType.PSYCHIC));
		d.setDamageDice(damageDice);
		Manager.processEvent(d, null);
		d.invoke(null);
		
		return d;
	}

}
