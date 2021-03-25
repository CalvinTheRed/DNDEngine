package com.dndsuite.core.gameobjects;

import java.util.LinkedList;

import com.dndsuite.core.Item;
import com.dndsuite.core.Observer;
import com.dndsuite.core.Scriptable;
import com.dndsuite.core.Subject;
import com.dndsuite.core.effects.Effect;
import com.dndsuite.dnd.VirtualBoard;
import com.dndsuite.maths.Vector;

public abstract class GameObject extends Scriptable implements Subject {
	// TODO: move the following values to another subclass of GameObject later
	public static final String OBJECT = "Object";
	public static final String STRUCTURE = "Structure";

	protected Vector pos;
	protected Vector rot;
	protected LinkedList<Effect> activeEffects;
	protected LinkedList<Item> inventory;
	protected LinkedList<Observer> observers;
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

	public Vector getPos() {
		return pos;
	}

	public Vector getRot() {
		return rot;
	}

	@Override
	public void addObserver(Observer o) {
		observers.add(o);
	}

	@Override
	public void removeObserver(Observer o) {
		observers.remove(o);
	}

	@Override
	public void updateObservers() {
		for (Observer o : observers) {
			o.update(this);
		}
	}

	public LinkedList<Item> getInventory() {
		return inventory;
	}

	public void addToInventory(Item item) {
		inventory.add(item);
	}

	public boolean removeFromInventory(Item item) {
		return inventory.remove(item);
	}

	/*
	 * -----------------------------------------------------------------------------
	 * Functions used from Lua define function -------------------------------------
	 * -----------------------------------------------------------------------------
	 */

	public void prepGameObject() {
		activeEffects = new LinkedList<Effect>();
		inventory = new LinkedList<Item>();
		observers = new LinkedList<Observer>();
	}

	public void setHealth(int health) {
		this.health = health;
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

}
