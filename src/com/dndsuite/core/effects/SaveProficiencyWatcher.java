package com.dndsuite.core.effects;

import com.dndsuite.core.events.Event;
import com.dndsuite.core.events.contests.SavingThrow;
import com.dndsuite.core.gameobjects.Entity;

public class SaveProficiencyWatcher extends Effect {
	public SaveProficiencyWatcher(Entity source) {
		super(null, source, source);
		name = "Saving Throw Proficiency Watcher";
		addTag(SaveProficiencyWatcher.getEffectID());
	}

	@Override
	public boolean processEvent(Event e, Entity source, Entity target) {
		try {
			if (e instanceof SavingThrow) {
				// SavingThrow st = (SavingThrow) e;
				// TODO: implement saving throw proficiencies and complete this
			}
		} catch (Exception ex) {
		}
		return false;
	}

	public static String getEffectID() {
		return "Save Proficiency Watcher";
	}

}
