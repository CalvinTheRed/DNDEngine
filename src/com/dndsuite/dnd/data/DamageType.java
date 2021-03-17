package com.dndsuite.dnd.data;

public enum DamageType {
	/**
	 * This damage type is intended to be used for damage dice which are to be added
	 * to another group of damage dice, when the damage type of those dice is not
	 * necessarily known. This is currently intended to be used for appending to the
	 * damage dice of a weapon's damage.
	 */
	MATCHING(),
	/**
	 * This damage type is intended to represent damage which does not have a
	 * specific type, or raw damage. This may come up from effects such as blood
	 * loss.
	 */
	TYPELESS(),
	/**
	 * This damage type is intended to represent damage which cannot be reduced,
	 * such as that suffered from the Oath of Redemption Paladin's Aura of the
	 * Guardian feature.
	 */
	INEVITABLE(),
	// normal in-game damage types
	ACID(),
	BLUDGEONING(),
	BLUDGEONING_MAGICAL(),
	BLUDGEONING_SILVERED(),
	BLUDGEONING_MAGICAL_SILVERED(),
	COLD(),
	FIRE(),
	FORCE(),
	LIGHTNING(),
	NECROTIC(),
	PIERCING(),
	PIERCING_MAGICAL(),
	PIERCING_SILVERED(),
	PIERCING_MAGICAL_SILVERED(),
	POISON(),
	PSYCHIC(),
	RADIANT(),
	SLASHING(),
	SLASHING_MAGICAL(),
	SLASHING_SILVERED(),
	SLASHING_MAGICAL_SILVERED(),
	THUNDER();
}
