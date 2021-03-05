package core.events;

import core.gameobjects.Entity;
import dnd.VirtualBoard;
import maths.Vector;

public abstract class AttackRoll extends DiceContest {
	public static final String EVENT_TAG_ID = "Attack Roll";

	public static final String MELEE = "Melee";
	public static final String RANGED = "Ranged";
	public static final String THROWN = "Thrown";

	public static final String CRITICAL_HIT = "Critical Hit";

	protected String attackType;
	protected int attackAbility;
	protected Entity target;

	public AttackRoll(String script, int attackAbility) {
		super(script);
		this.attackAbility = attackAbility;
		setRadius(0.0);
		addTag(Event.SINGLE_TARGET);
		addTag(AttackRoll.EVENT_TAG_ID);
	}

	public int getAttackAbility() {
		return attackAbility;
	}

	public String getAttackType() {
		return attackType;
	}

	public void setAttackType(String attackType) {
		this.attackType = attackType;
	}

	@Override
	public void invoke(Entity source, Vector targetPos) {
		target = VirtualBoard.entityAt(targetPos);

		while (source.processEvent(this, source, target) || target.processEvent(this, source, target))
			;

		// apply relevant ability score modifier as attack roll bonus
		addBonus(source.getAbilityModifier(attackAbility));
		roll();

		while (source.processEvent(this, source, target) || target.processEvent(this, source, target))
			;

		ArmorClassCalculation acc = new ArmorClassCalculation(source);
		source.processEvent(acc, source, source);
		if (getRoll() >= acc.getAC()) {
			System.out.println("[JAVA] Attack roll hit! (" + getRawRoll() + "+" + bonus + ":" + acc.getAC() + ")");
			invokeFallout(source);
		} else {
			System.out.println("[JAVA] Attack roll miss! (" + getRawRoll() + "+" + bonus + ":" + acc.getAC() + ")");
		}
	}

}
