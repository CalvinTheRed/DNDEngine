package dnd.items;

import dnd.combat.DamageDiceGroup;
import dnd.data.DamageType;
import dnd.data.WeaponProperty;

public class Club extends Item {
	
	public Club() {
		super("Club", new double[] {20.0, 40.0}, 5.0);
		weaponProperties.add(WeaponProperty.LIGHT);
	}
	
	@Override
	public DamageDiceGroup getDamageDice() {
		return new DamageDiceGroup(1, 4, DamageType.BLUDGEONING);
	}
	
}
