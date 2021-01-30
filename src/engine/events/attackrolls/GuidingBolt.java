package engine.events.attackrolls;

import java.util.LinkedList;

import dnd.combat.DamageDiceGroup;
import dnd.data.DamageType;
import dnd.items.Item;
import engine.Manager;
import engine.effects.GuidingBoltEffect;
import engine.events.Damage;
import gameobjects.entities.Entity;

public class GuidingBolt extends AttackRoll {

	public GuidingBolt(Entity source, Item medium, int attackAbilityScore) {
		super(source, medium, "Guiding Bolt", attackAbilityScore, true);
	}

	@Override
	protected void applyHit(Entity target) {
		Damage d = new Damage(source, target, name);
		LinkedList<DamageDiceGroup> damageDice = new LinkedList<DamageDiceGroup>();
		damageDice.add(new DamageDiceGroup(4, 6, DamageType.RADIANT));
		d.setDamageDice(damageDice);
		Manager.processEvent(d, target);
		d.invoke(null);
		target.processDamageEvent(d);
		
		Manager.addEffect(new GuidingBoltEffect(source, target));
	}

	@Override
	protected void applyMiss(Entity target) {
		// No effects on a miss!
	}

}
