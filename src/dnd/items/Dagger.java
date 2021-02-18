package dnd.items;

import dnd.combat.DamageDiceGroup;
import dnd.data.DamageType;
import dnd.data.WeaponProperty;

public class Dagger extends Item {
	
	public Dagger() {
		super("Dagger", new double[] {20.0, 60.0}, 5.0);
		weaponProperties.add(WeaponProperty.FINESSE);
		weaponProperties.add(WeaponProperty.LIGHT);
		weaponProperties.add(WeaponProperty.THROWN);
	}
	
	@Override
	public DamageDiceGroup getDamageDice() {
		return new DamageDiceGroup(1, 4, DamageType.PIERCING);
	}
	
}
