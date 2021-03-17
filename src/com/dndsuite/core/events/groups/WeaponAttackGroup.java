package com.dndsuite.core.events.groups;

import com.dndsuite.core.Item;
import com.dndsuite.core.Observer;
import com.dndsuite.core.Subject;
import com.dndsuite.core.events.contests.AttackRoll;
import com.dndsuite.core.events.contests.WeaponAttack;
import com.dndsuite.core.gameobjects.Entity;

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
