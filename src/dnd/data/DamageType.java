package dnd.data;

public enum DamageType {
	// meant for damage which adds to an existing damage roll
	// such as the Rogue class's Sneak Attack damage
	MATCHING(),
	// meant for damage which has no particular type
	// such as that dealt due to blood loss
	TYPELESS(),
	// meant for damage which cannot be reduced in any way
	// such as that suffered from the Oath of Redemption subclass's
	// Aura of the Guardian feature
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
