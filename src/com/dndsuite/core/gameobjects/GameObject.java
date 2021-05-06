package com.dndsuite.core.gameobjects;

import org.json.simple.JSONObject;

import com.dndsuite.core.json.JSONLoader;
import com.dndsuite.dnd.VirtualBoard;
import com.dndsuite.maths.Vector;

public class GameObject extends JSONLoader {
	protected Vector pos;
	protected Vector rot;

	public GameObject(JSONObject json, Vector pos, Vector rot) {
		super(json);
		this.pos = pos;
		this.rot = rot;
	}

	public GameObject(String file, Vector pos, Vector rot) {
		super("resources/json/gameobjects/" + file + ".json");
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

	@SuppressWarnings("unchecked")
	public String toString() {
		return (String) json.getOrDefault("name", "?");
	}

}
