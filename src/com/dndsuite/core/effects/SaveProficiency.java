package com.dndsuite.core.effects;

import com.dndsuite.core.events.Event;
import com.dndsuite.core.events.contests.SavingThrow;
import com.dndsuite.core.gameobjects.Entity;
import com.dndsuite.core.gameobjects.GameObject;

public class SaveProficiency extends Effect {
	protected int saveAbilityIndex;
	
	public SaveProficiency(Entity source, Entity target, int saveAbilityIndex) {
		super(null, source, target);
		this.saveAbilityIndex = saveAbilityIndex;
		setName(SaveProficiency.getEffectID() + " (" + Entity.getAbilityString(saveAbilityIndex) + ")");
		addTag(SaveProficiency.getEffectID());
	}
	
	public static String getEffectID() {
		return "Save Proficiency";
	}
	
	public void processEventSafe(Event e, Entity source, GameObject target) {
		try {
			if (e instanceof SavingThrow && target == getTarget()) {
				if (target instanceof Entity && e.getEventAbility() == saveAbilityIndex) {	
					SavingThrow st = (SavingThrow) e;
					st.applyEffect(this);
					st.addBonus(((Entity) target).getAbilityModifier(saveAbilityIndex));
				}
			}
		} catch (Exception ex) {
			System.out.println("[JAVA] Effect " + this + " already applied to event " + e);
		}
		
	}

}
