package core.effects;

import core.events.Event;
import core.events.SpellAttack;
import core.gameobjects.Entity;

public class SpellProficiencyWatcher extends Effect {

	public SpellProficiencyWatcher(Entity source) {
		super(null, source, source);
		name = "Spell Proficiency Watcher";
	}

	@Override
	public boolean processEvent(Event e, Entity source, Entity target) {
		try {
			if (e instanceof SpellAttack) {
				SpellAttack wa = (SpellAttack) e;
				wa.applyEffect(this);
				wa.addBonus(source.getProficiencyBonus());
				return true;
			} // TODO: else if e instanceof DiceCheckCalculation ...
		} catch (Exception ex) {
		}
		return false;
	}

}
