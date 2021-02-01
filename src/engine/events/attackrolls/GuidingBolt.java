package engine.events.attackrolls;

import dnd.items.Item;
import engine.effects.Effect;
import engine.effects.GuidingBoltEffect;
import gameobjects.entities.Entity;

public class GuidingBolt extends AttackRoll {

	public GuidingBolt(Entity source, Item medium, int attackAbilityScore) {
		super(source, medium, "Guiding Bolt", attackAbilityScore, true);
	}

	@Override
	protected void applyHit(Entity target) {
		Effect e = new GuidingBoltEffect(getSource(), target);
		target.observeEffect(e);
	}

	@Override
	protected void applyMiss(Entity target) {
		// No effects on a miss!
	}

}
