package dnd.items;

import dnd.combat.DamageDiceGroup;
import dnd.data.DamageType;
import dnd.data.WeaponProperty;

/**
 * A class representing the Club item
 * 
 * @author calvi
 *
 */
public class Club extends Item {

	/**
	 * Constructor for class Club
	 */
	public Club() {
		super("Club", new double[] { 20.0, 40.0 }, 5.0);
		weaponProperties.add(WeaponProperty.LIGHT);
	}

	@Override
	public DamageDiceGroup getDamageDice() {
		return new DamageDiceGroup(1, 4, DamageType.BLUDGEONING);
	}

}
