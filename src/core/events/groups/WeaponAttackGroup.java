package core.events.groups;

import core.Item;
import core.Observer;
import core.Subject;
import core.events.AttackRoll;
import core.events.WeaponAttack;
import core.gameobjects.Entity;

public class WeaponAttackGroup extends EventGroup implements Observer {

	public WeaponAttackGroup() {
		super();
	}

	@Override
	public void update(Subject s) {
		Item weapon = ((Entity) s).getMainhand();
		if (weapon == null) {
			events.clear();
			events.add(new WeaponAttack(null, Entity.STR, AttackRoll.MELEE, true));
		} else {
			events = weapon.getMainhandAttackOptions();
		}
	}

}
