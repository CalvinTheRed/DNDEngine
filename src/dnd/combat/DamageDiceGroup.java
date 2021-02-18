package dnd.combat;

import dnd.data.DamageType;
import maths.dice.DiceGroup;
import maths.dice.Die;

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
	protected int damageBonus;
	protected int resistances;
	protected int immunities;
	protected int vulnerabilities;

	/**
	 * Constructor for class DamageDiceGroup
	 * 
	 * @param numDice    (int) the number of dice to be included in the
	 *                   DamageDiceGroup upon construction
	 * 
	 * @param dieSize    (int) the number of sides on each die added to the
	 *                   DamageDiceGroup upon construction
	 * 
	 * @param damageType (DamageType) the damage type of the damage dice contained
	 *                   in this DamageDiceGroup
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
	 * @return DamageDiceGroup
	 * 
	 * @Override
	 */
	public DamageDiceGroup clone() {
		DamageDiceGroup group = new DamageDiceGroup(0, 0, damageType);
		group.addDamageBonus(damageBonus);
		for (Die d : dice) {
			group.addDamageDie(d.clone());
		}
		return group;
	}

	/**
	 * This function requires roll() to be called before it reports any meaningful
	 * data. It returns the sum of all contained dice plus the contained damage
	 * bonus.
	 * 
	 * @return int
	 * 
	 * @Override
	 */
	public int getSum() {
		return Math.max(1, super.getSum() + damageBonus);
	}

	/**
	 * This function returns the contained damage bonus.
	 * 
	 * @return int
	 */
	public int getDamageBonus() {
		return damageBonus;
	}

	/**
	 * This function adds to the contained damage bonus. It does not overwrite the
	 * damage bonus.
	 * 
	 * @param bonus (int) the damage bonus to be added to the current damage bonus.
	 */
	public void addDamageBonus(int bonus) {
		damageBonus += bonus;
	}

	/**
	 * This function returns the damage type of the contained damage dice.
	 * 
	 * @return DamageType
	 */
	public DamageType getDamageType() {
		return damageType;
	}

	/**
	 * This function modifies the damage type for the contained damage dice.
	 * 
	 * @param damageType (DamageType) the new damage type for the contained damage
	 *                   dice
	 */
	public void setDamageType(DamageType damageType) {
		this.damageType = damageType;
	}

	/**
	 * This function adds a new damage die to the object. It does not have to have
	 * the same number of sides as the other dice contained in the object.
	 * 
	 * @param d (Die) the die to be added to the object
	 */
	public void addDamageDie(Die d) {
		dice.add(d);
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
	 * @return int
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

	// TODO: add function to halve damage here, then replace in Evasion and Fireball

	/**
	 * This function is a specialized form of setDamageType(). It makes the damage
	 * type magical, if applicable.
	 * 
	 * @return boolean
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
	 * @return boolean
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
	 * @return boolean
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
	 * @return boolean
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
