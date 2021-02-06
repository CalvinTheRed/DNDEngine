package dnd.items.weapons;

import dnd.combat.DamageDiceGroup;
import dnd.data.DamageType;
import dnd.items.Item;

public class Longsword extends Item {

	public Longsword() {
		super("Longsword");
		isWeapon = true;
	}
	
	public DamageDiceGroup getDamageDice() {
		return new DamageDiceGroup(1, 8, DamageType.SLASHING);
	}
	
	public DamageDiceGroup getDamageDiceVersatile() {
		return new DamageDiceGroup(1, 8, DamageType.SLASHING);
	}

}
