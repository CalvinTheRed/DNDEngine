package dnd.events.eventgroups;

import dnd.events.dicecontests.attackroll.WeaponAttack;
import dnd.items.Inventory;
import dnd.items.Item;
import engine.patterns.Observer;
import engine.patterns.Subject;
import gameobjects.entities.Entity;

/**
 * A specialized form of EventGroup which is dedicated to specifically
 * containing Event options pertaining to making weapon attacks
 * 
 * @author calvi
 *
 */
public class WeaponAttackGroup extends EventGroup implements Observer {

	/**
	 * Constructor for class WeaponAttackGroup
	 */
	public WeaponAttackGroup() {
		super();
	}

	@Override
	public void update(Subject s) {
		Item weapon = ((Inventory) s).mainhand();
		if (weapon == null) {
			events.clear();
			events.add(new WeaponAttack(null, WeaponAttack.MELEE, Entity.STR));
		} else {
			events = weapon.getAttackOptions();
		}
	}

}
