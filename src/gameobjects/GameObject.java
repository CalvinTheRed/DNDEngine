package gameobjects;

import engine.Manager;
import maths.Vector;

public abstract class GameObject {
	protected String name;
	protected Vector pos;
	protected Vector rot;
	
	public GameObject(String name, Vector pos, Vector rot) {
		this.name = name;
		this.pos = pos;
		this.rot = rot;
		
		Manager.addGameObject(this);
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public Vector getPos() {
		return pos;
	}
	
	public Vector getRot() {
		return rot;
	}
	
}
