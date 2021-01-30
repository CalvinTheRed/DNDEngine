package dnd.data;

public enum Condition {
	BLINDED(),
	CHARMED(),
	DEAFENED(),
	FRIGHTENED(),
	GRAPPLED(),
	INCAPACITATED(),
	INVISIBLE(),
	PARALYZED(),
	PETRIFIED(),
	POISONED(),
	PRONE(),
	RESTRAINED(),
	STUNNED(),
	UNCONSCIOUS(),
	// Exhaustion has 6 cumulative levels,
	// each of which is treated as a discrete
	// condition. Immunity to exhaustion can
	// be attained simply with immunity to
	// EXHAUSTION_1, as EXHAUSTION_2 and
	// beyond is contingent upon the prior
	// levels being present.
	EXHAUSTION_1(),
	EXHAUSTION_2(),
	EXHAUSTION_3(),
	EXHAUSTION_4(),
	EXHAUSTION_5(),
	EXHAUSTION_6();
}
