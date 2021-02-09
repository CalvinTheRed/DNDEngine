package dnd.events.eventgroups;

import dnd.events.dicecontest.MeleeWeaponAttack;
import dnd.items.Inventory;
import dnd.items.Item;
import engine.patterns.Observer;
import engine.patterns.Subject;

public class WeaponAttackGroup extends EventGroup implements Observer {
	
	public WeaponAttackGroup() {
		super();
	}

	@Override
	public void update(Subject s) {
		Item weapon = ((Inventory)s).mainhand();
		if (weapon == null) {
			events.clear();
			events.add(new MeleeWeaponAttack(null));
		}
		else {
			events = weapon.getAttackOptions();
		}
	}
	
}
