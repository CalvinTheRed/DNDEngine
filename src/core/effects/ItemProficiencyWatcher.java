package core.effects;

import core.Item;
import core.events.ArmorClassCalculation;
import core.events.Event;
import core.events.contests.WeaponAttack;
import core.gameobjects.Entity;

public class ItemProficiencyWatcher extends Effect {
	public ItemProficiencyWatcher(Entity source) {
		super(null, source, source);
		name = "Item Proficiency Watcher";
		addTag(ItemProficiencyWatcher.getEffectID());
	}

	@Override
	public boolean processEvent(Event e, Entity source, Entity target) {
		try {
			if (e instanceof WeaponAttack) {
				WeaponAttack wa = (WeaponAttack) e;
				try {
					for (String tag : wa.getMedium().getTags()) {
						if (source.proficientWith(tag)) {
							wa.applyEffect(this);
							wa.addBonus(source.getProficiencyBonus());
							return true;
						}
					}
				} catch (NullPointerException ex) {
					// handling exception for unarmed attacks when wa.getMedium() returns null
					wa.applyEffect(this);
					wa.addBonus(source.getProficiencyBonus());
					return true;
				}

			} else if (e instanceof ArmorClassCalculation) {
				ArmorClassCalculation acc = (ArmorClassCalculation) e;
				Entity parent = acc.getParent();
				Item armor = parent.getArmor();
				try {
					for (String tag : armor.getTags()) {
						if (parent.proficientWith(tag)) {
							acc.applyEffect(this);
							acc.setBaseAC(armor.getAC());
							acc.setAbilityBonusLimit(armor.getACAbilityBonusLimit());
							return true;
						}
					}
				} catch (NullPointerException ex) {
					// handling exception for when the subject is not wearing armor and
					// parent.getArmor() returns null
					return false;
				}
			}
		} catch (Exception ex) {
		}
		return false;
	}

	public static String getEffectID() {
		return "Item Proficiency Watcher";
	}

}
