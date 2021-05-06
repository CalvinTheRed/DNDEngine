package com.dndsuite.dnd.combat;

import com.dndsuite.dnd.data.DamageType;
import com.dndsuite.maths.dice.DiceGroup;
import com.dndsuite.maths.dice.Die;

/**
 * A class dedicated to encapsulating a collection of damage dice of a common
 * damage type. This class contains a collection of dice of arbitrary size, as
 * well as a damage bonus and its status in relation to resistances,
 * vulnerabilities, and immunities.
 * 
 * @author calvi
 *
 */
public class DamageDiceGroup extends DiceGroup {

	public static final int NORMAL = 0;
	public static final int RESISTED = 1;
	public static final int ENHANCED = 2;
	public static final int NEUTRALIZED = 3;
	public static final int NO_EFFECT = 4;

	protected DamageType damageType;
	protected int resistances;
	protected int immunities;
	protected int vulnerabilities;

	/**
	 * Constructor for class DamageDiceGroup
	 * 
	 * @param numDice    ({@code int}) the number of dice to be included in the
	 *                   DamageDiceGroup upon construction
	 * 
	 * @param dieSize    ({@code int}) the number of sides on each die added to the
	 *                   DamageDiceGroup upon construction
	 * 
	 * @param damageType ({@code DamageType}) the damage type of the damage dice
	 *                   contained in this DamageDiceGroup
	 */
	public DamageDiceGroup(int numDice, int dieSize, DamageType damageType) {
		super(numDice, dieSize);
		this.damageType = damageType;
		resistances = 0;
		immunities = 0;
		vulnerabilities = 0;
	}

	/**
	 * A function for creating a deep clone of the calling Damage object. This clone
	 * contains clones of all of its fields, rather than identical memory addresses
	 * to the same fields as its parent instance.
	 * 
	 * @return {@code DamageDiceGroup} clone
	 * 
	 * @Override
	 */
	public DamageDiceGroup clone() {
		DamageDiceGroup group = new DamageDiceGroup(0, 0, damageType);
		group.addBonus(bonus);
		for (Die d : dice) {
			group.addDie(d.clone());
		}
		return group;
	}

	public void makeVersatile() {
		for (Die d : dice) {
			d.upsize();
		}
	}

	/**
	 * This function returns the damage type of the contained damage dice.
	 * 
	 * @return {@code DamageType} damageType
	 */
	public DamageType getDamageType() {
		return damageType;
	}

	/**
	 * This function modifies the damage type for the contained damage dice.
	 * 
	 * @param damageType ({@code DamageType}) the new damage type for the contained
	 *                   damage dice
	 */
	public void setDamageType(DamageType damageType) {
		this.damageType = damageType;
	}

	/**
	 * This function increments the resistance counter by 1, indicating that this
	 * damage is being resisted somehow.
	 */
	public void grantResistance() {
		resistances++;
	}

	/**
	 * This function increments the immunity counter by 1, indicating that this
	 * damage is being disregarded somehow.
	 */
	public void grantImmunity() {
		immunities++;
	}

	/**
	 * This function increments the vulnerability counter by 1, indicating that this
	 * damage is being enhanced somehow.
	 */
	public void grantVulnerability() {
		vulnerabilities++;
	}

	/**
	 * This function returns how effective the damage is with respect to
	 * resistances, vulnerabilities, and immunities.
	 * 
	 * @return {@code int} effectiveness id [0,4]
	 */
	public int getEffectiveness() {
		if (immunities > 0) {
			return NO_EFFECT;
		}
		if (resistances > 0 && vulnerabilities > 0) {
			return NEUTRALIZED;
		}
		if (resistances > 0) {
			return RESISTED;
		}
		if (vulnerabilities > 0) {
			return ENHANCED;
		}
		return NORMAL;
	}

	// TODO: verify this function works as intended
	public void halve() {
		int sum = this.getSum();
		addBonus(-(sum + 1) / 2);
	}

	/**
	 * This function is a specialized form of setDamageType(). It makes the damage
	 * type magical, if applicable.
	 * 
	 * @return {@code boolean} was this operation successful?
	 */
	public boolean grantMagic() {
		if (damageType == DamageType.BLUDGEONING) {
			damageType = DamageType.BLUDGEONING_MAGICAL;
		} else if (damageType == DamageType.BLUDGEONING_SILVERED) {
			damageType = DamageType.BLUDGEONING_MAGICAL_SILVERED;
		} else if (damageType == DamageType.PIERCING) {
			damageType = DamageType.PIERCING_MAGICAL;
		} else if (damageType == DamageType.PIERCING_SILVERED) {
			damageType = DamageType.PIERCING_MAGICAL_SILVERED;
		} else if (damageType == DamageType.SLASHING) {
			damageType = DamageType.SLASHING_MAGICAL;
		} else if (damageType == DamageType.SLASHING_SILVERED) {
			damageType = DamageType.SLASHING_MAGICAL_SILVERED;
		} else {
			System.out.println("ERR: damage type " + damageType + " cannot be made magical");
			return false;
		}
		return true;
	}

	/**
	 * This function is a specialized form of setDamageType(). It takes away the
	 * magical property from the damage type, if applicable.
	 * 
	 * @return {@code boolean} was this operation successful?
	 */
	public boolean revokeMagic() {
		if (damageType == DamageType.BLUDGEONING_MAGICAL) {
			damageType = DamageType.BLUDGEONING;
		} else if (damageType == DamageType.BLUDGEONING_MAGICAL_SILVERED) {
			damageType = DamageType.BLUDGEONING_SILVERED;
		} else if (damageType == DamageType.PIERCING_MAGICAL) {
			damageType = DamageType.PIERCING;
		} else if (damageType == DamageType.PIERCING_MAGICAL_SILVERED) {
			damageType = DamageType.PIERCING_SILVERED;
		} else if (damageType == DamageType.SLASHING_MAGICAL) {
			damageType = DamageType.SLASHING;
		} else if (damageType == DamageType.SLASHING_MAGICAL_SILVERED) {
			damageType = DamageType.SLASHING_SILVERED;
		} else {
			System.out.println("ERR: damage type " + damageType + " cannot be made non-magical");
			return false;
		}
		return true;
	}

	/**
	 * This function is a specialized form of setDamageType(). It makes the damage
	 * type silvered, if applicable.
	 * 
	 * @return {@code boolean} was this operation successful?
	 */
	public boolean grantSilvered() {
		if (damageType == DamageType.BLUDGEONING) {
			damageType = DamageType.BLUDGEONING_SILVERED;
		} else if (damageType == DamageType.BLUDGEONING_MAGICAL) {
			damageType = DamageType.BLUDGEONING_MAGICAL_SILVERED;
		} else if (damageType == DamageType.PIERCING) {
			damageType = DamageType.PIERCING_SILVERED;
		} else if (damageType == DamageType.PIERCING_MAGICAL) {
			damageType = DamageType.PIERCING_MAGICAL_SILVERED;
		} else if (damageType == DamageType.SLASHING) {
			damageType = DamageType.SLASHING_SILVERED;
		} else if (damageType == DamageType.SLASHING_MAGICAL) {
			damageType = DamageType.SLASHING_MAGICAL_SILVERED;
		} else {
			System.out.println("ERR: damage type " + damageType + " cannot be made silvered");
			return false;
		}
		return true;
	}

	/**
	 * This function is a specialized form of setDamageType(). It takes away the
	 * silvered property from the damage type, if applicable.
	 * 
	 * @return {@code boolean} was this operation successful?
	 */
	public boolean revokeSilvered() {
		if (damageType == DamageType.BLUDGEONING_SILVERED) {
			damageType = DamageType.BLUDGEONING;
		} else if (damageType == DamageType.BLUDGEONING_MAGICAL_SILVERED) {
			damageType = DamageType.BLUDGEONING_MAGICAL;
		} else if (damageType == DamageType.PIERCING_SILVERED) {
			damageType = DamageType.PIERCING;
		} else if (damageType == DamageType.PIERCING_MAGICAL_SILVERED) {
			damageType = DamageType.PIERCING_MAGICAL;
		} else if (damageType == DamageType.SLASHING_SILVERED) {
			damageType = DamageType.SLASHING;
		} else if (damageType == DamageType.SLASHING_MAGICAL_SILVERED) {
			damageType = DamageType.SLASHING_MAGICAL;
		} else {
			System.out.println("ERR: damage type " + damageType + " cannot be made non-silvered");
			return false;
		}
		return true;
	}

}
