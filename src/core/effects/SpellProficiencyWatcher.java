package core.effects;

import core.events.Event;
import core.events.contests.AttackRoll;
import core.gameobjects.Entity;

public class SpellProficiencyWatcher extends Effect {

	public SpellProficiencyWatcher(Entity source) {
		super(null, source, source);
		name = "Spell Proficiency Watcher";
	}

	@Override
	public boolean processEvent(Event e, Entity source, Entity target) {
		try {
			if (e instanceof AttackRoll) {
				AttackRoll ar = (AttackRoll) e;
				if (ar.hasTag(AttackRoll.SPELL)) {
					ar.applyEffect(this);
					ar.addBonus(source.getProficiencyBonus());
					return true;
				}
			} // TODO: else if e instanceof DiceCheckCalculation ...
		} catch (Exception ex) {
		}
		return false;
	}

}
