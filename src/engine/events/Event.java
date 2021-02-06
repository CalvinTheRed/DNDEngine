package engine.events;

import java.util.LinkedList;

import engine.effects.Effect;
import gameobjects.entities.Entity;
import maths.dice.Die;

public abstract class Event {
	public static final int HAS_DADV = -1;
	public static final int NO_ADV_MODS = 0;
	public static final int HAS_ADV = 1;
	public static final int HAS_ADV_AND_DADV = 2;
	
	private Entity source;
	private String name;
	private LinkedList<Entity> targets;
	private LinkedList<Effect> appliedEffects;
	private int advantage;
	private int disadvantage;
	
	protected Die d20;
	
	public Event(Entity source, String name) {
		this.source = source;
		this.name = name;
		targets = new LinkedList<Entity>();
		appliedEffects = new LinkedList<Effect>();
		d20 = new Die(20);
	}
	
	public Entity getSource() {
		return source;
	}
	
	public LinkedList<Entity> getTargets(){
		return targets;
	}
	
	public boolean isEffectApplied(Effect e) {
		return appliedEffects.contains(e);
	}
	
	public void applyEffect(Effect e) {
		appliedEffects.add(e);
	}
	
	public void clearAppliedEffects() {
		appliedEffects.clear();
	}
	
	public void grantAdvantage() {
		advantage++;
	}
	
	public void grantDisadvantage() {
		disadvantage++;
	}
	
	public void clearAdvantageMods() {
		advantage = 0;
		disadvantage = 0;
	}
	
	protected int getAdvantageState() {
		if (advantage > 0 && disadvantage > 0) {
			return HAS_ADV_AND_DADV;
		}
		if (advantage > 0) {
			return HAS_ADV;
		}
		if (disadvantage > 0) {
			return HAS_DADV;
		}
		return NO_ADV_MODS;
	}
	
	public abstract void invoke(LinkedList<Entity> targets);
	protected abstract void reset();
	
	@Override
	public String toString() {
		return name;
	}
}
