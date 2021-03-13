package core.effects;

import core.events.Event;
import core.events.contests.SavingThrow;
import core.gameobjects.Entity;

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
