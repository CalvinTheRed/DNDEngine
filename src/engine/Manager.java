package engine;

import java.util.LinkedList;

import gameobjects.GameObject;
import gameobjects.entities.Entity;
import maths.Vector;

public final class Manager {
	
	private static final double CONE_ARC_SIZE = Math.toRadians(45);
	
	private static LinkedList<GameObject> gameObjects;
	
	public static void initialize() {
		gameObjects = new LinkedList<GameObject>();
	}
	
	public static boolean addGameObject(GameObject o) {
		if (gameObjects.contains(o)) {
			return false;
		}
		gameObjects.add(o);
		return true;
	}
	
	public static boolean removeGameObject(GameObject o) {
		return gameObjects.remove(o);
	}
	
	public static LinkedList<Entity> entitiesInCube(Vector center, Vector rot, double radius) {
		LinkedList<Entity> entities = new LinkedList<Entity>();
		Vector axis1 = rot.unit().scale(radius);
		Vector axis2 = axis1.cross(Vector.UNIT_Y);
		System.out.println("Searching for Entities in cube centered at " + center + " with radius vector " + axis1);
		System.out.println("Defined by base corners "
				+ center.add(axis1).add(axis2) + ", "
				+ center.add(axis1).sub(axis2) + ", "
				+ center.sub(axis1).add(axis2) + ", "
				+ center.sub(axis1).sub(axis2) + ":"
		);
		for (GameObject o : gameObjects) {
			if (o instanceof Entity) {
				Vector deltaPos = o.getPos().sub(center);
				if (deltaPos.proj(axis1).mag() <= radius && deltaPos.proj(axis2).mag() <= radius && deltaPos.proj(Vector.UNIT_Y).mag() <= radius) {
					System.out.println("Found " + o + " " + o.getPos());
					entities.add((Entity)o);
				}
			}
		}
		return entities;
	}
	
	public static LinkedList<Entity> entitiesInSphere(Vector origin, double radius) {
		System.out.println("Searching for Entities in sphere centered at " + origin + " with radius " + radius + ":");
		LinkedList<Entity> entities = new LinkedList<Entity>();
		for (GameObject o : gameObjects) {
			if (o instanceof Entity && o.getPos().sub(origin).mag() <= radius) {
				System.out.println("Found " + o + " " + o.getPos());
				entities.add((Entity)o);
			}
		}
		return entities;
	}
	
	public static LinkedList<Entity> entitiesInCone(Vector origin, double radius, Vector rot) {
		LinkedList<Entity> entities = entitiesInSphere(origin, radius);
		System.out.println("Refining search to match cone in direction " + rot.unit() + " within angle " + Math.toDegrees(CONE_ARC_SIZE / 2.0) + " degrees:");
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i).getPos().sub(origin).calculateAngleDiff(rot) > CONE_ARC_SIZE / 2.0) {
				entities.remove(i);
				i--;
			}
			else {
				System.out.println("Found " + entities.get(i) + " " + entities.get(i).getPos());
			}
		}
		return entities;
	}
	
}
