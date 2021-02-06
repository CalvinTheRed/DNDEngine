package engine.events.attackrolls;

import java.util.LinkedList;

import dnd.combat.DamageDiceGroup;
import dnd.data.DamageType;
import dnd.items.Item;
import engine.effects.Effect;
import engine.effects.GuidingBoltEffect;
import engine.events.Damage;
import gameobjects.entities.Entity;

public class GuidingBolt extends AttackRoll {

	public GuidingBolt(Entity source, Item medium, int attackAbilityScore) {
		super(source, medium, "Guiding Bolt", attackAbilityScore, true);
	}

	@Override
	protected void applyHit(Entity target) {
		Effect e = new GuidingBoltEffect(getSource(), target);
		target.observeEffect(e);
		
		Damage d = new Damage(getSource(), this);
		LinkedList<DamageDiceGroup> damageDice = new LinkedList<DamageDiceGroup>();
		damageDice.add(new DamageDiceGroup(3, 8, DamageType.RADIANT));
		d.setDamageDice(damageDice);
		d.invoke(null);
		target.processDamageEvent(d);
	}

	@Override
	protected void applyMiss(Entity target) {
		// No effects on a miss!
	}
	
}
