package core.effects;

import core.events.Event;
import core.events.contests.SavingThrow;
import core.gameobjects.Entity;

public class SaveProficiencyWatcher extends Effect {

	public SaveProficiencyWatcher(Entity source) {
		super(null, source, source);
		name = "Saving Throw Proficiency Watcher";
	}

	@Override
	public boolean processEvent(Event e, Entity source, Entity target) {
		try {
			if (e instanceof SavingThrow) {
				SavingThrow st = (SavingThrow) e;
				// TODO: implement saving throw proficiencies and complete this
				st.applyEffect(this);
				return true;
			}
		} catch (Exception ex) {
		}
		return false;
	}

}
