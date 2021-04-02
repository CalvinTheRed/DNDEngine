package com.dndsuite.core.gameobjects;

import java.util.LinkedList;

import com.dndsuite.core.Item;
import com.dndsuite.core.Observer;
import com.dndsuite.core.Scriptable;
import com.dndsuite.core.Subject;
import com.dndsuite.core.effects.Effect;
import com.dndsuite.core.events.Damage;
import com.dndsuite.core.events.DamageCalculation;
import com.dndsuite.core.events.Event;
import com.dndsuite.dnd.VirtualBoard;
import com.dndsuite.dnd.combat.DamageDiceGroup;
import com.dndsuite.maths.Vector;

public abstract class GameObject extends Scriptable implements Subject {
	protected LinkedList<Effect> activeEffects;
	protected LinkedList<Item> inventory;
	protected LinkedList<Observer> observers;
	protected Vector pos;
	protected Vector rot;
	protected int health;
	protected int healthBase;
	protected int healthMax;
	protected int healthTmp;

	public GameObject(String script, Vector pos, Vector rot) {
		super(script);
		this.pos = pos;
		this.rot = rot;
		VirtualBoard.addGameObject(this);
	}

	@Override
	public void addObserver(Observer o) {
		observers.add(o);
	}
	
	public void addToInventory(Item item) {
		inventory.add(item);
	}
	
	public void clearEndedEffects() {
		for (int i = 0; i < activeEffects.size(); i++) {
			if (activeEffects.get(i).isEnded()) {
				activeEffects.remove(i);
				i--;
			}
		}
	}
	
	public int getHealth() {
		return health;
	}

	public int getHealthBase() {
		return healthBase;
	}

	public int getHealthMax() {
		return healthMax;
	}

	public int getHealthTmp() {
		return healthTmp;
	}
	
	public int getHealthTotal() {
		return health + healthTmp;
	}
	
	public LinkedList<Item> getInventory() {
		return inventory;
	}
	
	public Vector getPos() {
		return pos;
	}

	public Vector getRot() {
		return rot;
	}
	
	public void processDamage(DamageCalculation dc) {
		for (DamageDiceGroup group : dc.getDamageDice()) {
			int adjustment;
			if (group.getEffectiveness() == DamageDiceGroup.RESISTED) {
				adjustment = group.getSum() - Math.max(1, group.getSum() / 2);
				group.addBonus(adjustment);
			} else if (group.getEffectiveness() == DamageDiceGroup.ENHANCED) {
				adjustment = group.getSum();
				group.addBonus(adjustment);
			} else if (group.getEffectiveness() == DamageDiceGroup.NO_EFFECT) {
				adjustment = -group.getSum();
				group.addBonus(adjustment);
			}
			System.out.println("[JAVA] " + this + " takes " + group.getSum() + " " + group.getDamageType() + " damage");
		}
	}
	
	public boolean processEvent(Event e, Entity source, GameObject target) {
		for (Effect effect : activeEffects) {
			effect.processEvent(e, source, target);
		}
		return false;
	}
	
	public boolean removeFromInventory(Item item) {
		return inventory.remove(item);
	}

	@Override
	public void removeObserver(Observer o) {
		observers.remove(o);
	}
	
	public void setHealth(int health) {
		this.health = health;
		if (this.health > healthMax) {
			this.health = healthMax;
		}
	}

	public void setHealthBase(int healthBase) {
		this.healthBase = healthBase;
	}

	public void setHealthMax(int healthMax) {
		this.healthMax = healthMax;
	}

	public void setHealthTmp(int healthTmp) {
		this.healthTmp = healthTmp;
	}
	
	@Override
	protected void setup() {
		super.setup();
		activeEffects = new LinkedList<Effect>();
		inventory = new LinkedList<Item>();
		observers = new LinkedList<Observer>();
	}
	
	public void takeDamage(Damage d) {
		int damage = d.getDamage();
		if (healthTmp > 0) {
			int delta = healthTmp - damage;
			if (delta <= 0) {
				healthTmp = 0;
				damage += delta;
			}
			health -= damage;
			if (health <= 0) {
				if (d.hasTag(Damage.WITHSTOOD)) {
					health = 1;
				} else {
					health = 0;
					// TODO: GameObject death stuff here
				}
			}
		}
	}

	@Override
	public void updateObservers() {
		for (Observer o : observers) {
			o.update(this);
		}
	}

}
