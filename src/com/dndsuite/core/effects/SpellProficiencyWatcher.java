package com.dndsuite.core.effects;

import com.dndsuite.core.events.DiceCheckCalculation;
import com.dndsuite.core.events.Event;
import com.dndsuite.core.events.contests.AttackRoll;
import com.dndsuite.core.gameobjects.Entity;

public class SpellProficiencyWatcher extends Effect {
	public SpellProficiencyWatcher(Entity source) {
		super(null, source, source);
		name = "Spell Proficiency Watcher";
		addTag(SpellProficiencyWatcher.getEffectID());
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

	public static String getEffectID() {
		return "Spell Proficiency Watcher";
	}

}
