package com.dndsuite.core.gameobjects;

import com.dndsuite.dnd.VirtualBoard;
import com.dndsuite.maths.Vector;

/**
 * An abstract class representing a game piece on the game board (a player, a
 * zombie, a wall, a tree, a barrel, an item, etc)
 * 
 * @author calvi
 *
 */
public abstract class GameObject {
	protected String name;
	protected Vector pos;
	protected Vector rot;

	/**
	 * Constructor for class GameObject
	 * 
	 * @param name ({@code String}) the name of the GameObject
	 * @param pos  ({@code Vector}) the coordinate at which the GameObject is
	 *             located
	 * @param rot  ({@code Vector}) a Vector in parallel with the direction the
	 *             GameObject is facing
	 */
	public GameObject(String name, Vector pos, Vector rot) {
		this.name = name;
		this.pos = pos;
		this.rot = rot;

		VirtualBoard.addGameObject(this);
	}

	@Override
	public String toString() {
		return name;
	}

	/**
	 * This function returns the current position of the GameObject
	 * 
	 * @return {@code Vector} pos
	 */
	public Vector getPos() {
		return pos;
	}

	/**
	 * This function returns the current rotation of the GameObject
	 * 
	 * @return {@code Vector} rot
	 */
	public Vector getRot() {
		return rot;
	}

}
