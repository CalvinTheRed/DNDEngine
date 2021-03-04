package core.events;

import core.gameobjects.Entity;
import dnd.VirtualBoard;
import maths.Vector;

public abstract class AttackRoll extends DiceContest {
	public static final String MELEE = "Melee";
	public static final String RANGED = "Ranged";
	public static final String THROWN = "Thrown";

	protected int attackAbility;
	protected Entity target;

	public AttackRoll(String script, int attackAbility) {
		super(script, Event.SINGLE_TARGET);
		this.attackAbility = attackAbility;
		setRadius(0.0);
		addTag("Attack Roll");
	}

	@Override
	public void invoke(Entity source, Vector targetPos) {
		target = VirtualBoard.entityAt(targetPos);

		while (source.processEvent(this, source, target) || target.processEvent(this, source, target))
			;
		roll();
		while (source.processEvent(this, source, target) || target.processEvent(this, source, target))
			;

		// TODO: make an attack roll check properly here
		invokeFallout(source);
	}

}
