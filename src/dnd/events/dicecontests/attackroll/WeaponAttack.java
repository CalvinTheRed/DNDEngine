package dnd.events.dicecontests.attackroll;

import dnd.combat.DamageDiceGroup;
import dnd.events.Damage;
import dnd.items.Item;
import gameobjects.entities.Entity;

/**
 * A class representing attack rolls made by weapons, as opposed to ranged or
 * melee spells.
 * 
 * @author calvi
 *
 */
public class WeaponAttack extends AttackRoll {

	public static final int MELEE = 0;
	public static final int THROWN = 1;
	public static final int RANGED = 2;

	protected Item weapon;
	protected int attackType;

	/**
	 * Constructor for class WeaponAttack
	 * 
	 * @param weapon        ({@code Item}) the item being used to make the weapon
	 *                      attack
	 * @param attackType    ({@code int}) index indicating whether this weapon
	 *                      attack is melee, thrown, or ranged
	 * @param attackAbility ({@code int}) index indicating the ability score being
	 *                      used to make the weapon attack
	 */
	public WeaponAttack(Item weapon, int attackType, int attackAbility) {
		super((attackType == MELEE ? "Melee" : (attackType == THROWN ? "Thrown" : "Ranged")) + " Attack ("
				+ (weapon == null ? "Fist" : weapon.toString()) + ", " + Entity.getAbility(attackAbility) + ")",
				attackAbility);
		this.weapon = weapon;
		this.attackType = attackType;
		setRange(weapon == null ? 5.0 : weapon.getReach(), weapon == null ? 5.0 : weapon.getReach());
	}

	@Override
	protected void invokeFallout(Entity source) {
		Damage d = new Damage("Damage (" + this + ")", this);
		DamageDiceGroup damageDice = weapon.getDamageDice();
		damageDice.addBonus(source.getAbilityModifier(attackAbility));
		d.addDamageDiceGroup(damageDice);
		d.invoke(source, null);
		d.invokeClone(source, target);
	}

	/**
	 * This function returns whether this weapon attack is melee, thrown, or ranged.
	 * 
	 * @return {@code int} attack type id [0,3]
	 */
	public int getAttackType() {
		return attackType;
	}

}
