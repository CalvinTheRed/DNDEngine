package core.effects;

import core.events.DiceCheckCalculation;
import core.events.Event;
import core.events.contests.AttackRoll;
import core.gameobjects.Entity;

public class SpellProficiencyWatcher extends Effect {
	public static final String EFFECT_TAG_ID = "Spell Proficiency Watcher";

	public SpellProficiencyWatcher(Entity source) {
		super(null, source, source);
		name = "Spell Proficiency Watcher";
		addTag(EFFECT_TAG_ID);
	}

	@Override
	public boolean processEvent(Event e, Entity source, Entity target) {
		try {
			if (e instanceof AttackRoll) {
				AttackRoll ar = (AttackRoll) e;
				if (ar.hasTag(AttackRoll.SPELL) && source == getTarget()) {
					ar.applyEffect(this);
					ar.addBonus(source.getProficiencyBonus());
					return true;
				}
			} else if (e instanceof DiceCheckCalculation) {
				DiceCheckCalculation dcc = (DiceCheckCalculation) e;
				if (target == getTarget()) {
					dcc.applyEffect(this);
					dcc.addBonus(source.getProficiencyBonus());
					return true;
				}
			}
		} catch (Exception ex) {
		}
		return false;
	}

}