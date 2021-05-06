package com.dndsuite.dnd;

import java.util.LinkedList;

import com.dndsuite.core.gameobjects.GameObject;
import com.dndsuite.maths.Vector;

/**
 * This static class represents the game board upon which the game is being
 * played. It is responsible for containing all GameObject objects and for
 * finding which GameObject objects are within a particular area.
 * 
 * @author calvi
 * 
 */
public final class VirtualBoard {
	public static final double CONE_ARC_SIZE = Math.toRadians(45);

	private static LinkedList<GameObject> gameObjects = new LinkedList<GameObject>();
	// TODO: consider adding queuedGlobalEvents list here

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

	public static void clearGameObjects() {
		gameObjects.clear();
	}

	public static boolean contains(GameObject o) {
		return gameObjects.contains(o);
	}

	public static GameObject nearestObject(Vector pos, String[] filterTags) {
		GameObject nearest = null;
		double distance = Double.MAX_VALUE;
		for (GameObject o : gameObjects) {
			if (o.getPos().sub(pos).mag() < distance) {
				boolean valid = true;
				for (String tag : filterTags) {
//					if (!o.hasTag(tag)) {
//						valid = false;
//						break;
//					}
				}
				if (valid) {
					nearest = o;
					distance = nearest.getPos().sub(pos).mag();
				}
			}
		}
		return nearest;
	}

	public static LinkedList<GameObject> objectsInCone(Vector origin, Vector rot, double length, String[] filterTags) {
		LinkedList<GameObject> objects = new LinkedList<GameObject>();
		for (GameObject o : gameObjects) {
			Vector deltaPos = o.getPos().sub(origin);
			double radius = Math.sin(CONE_ARC_SIZE / 2) * deltaPos.proj(rot).mag();
			if (deltaPos.cross(rot).mag() / rot.mag() <= radius
					&& deltaPos.calculateAngleDiff(rot) <= Math.toRadians(90) && deltaPos.proj(rot).mag() <= length) {
				if (matchesFilterTags(o, filterTags)) {
					objects.add(o);
				}
			}
		}
		return objects;
	}

	public static LinkedList<GameObject> objectsInCube(Vector center, Vector rot, double radius, String[] filterTags) {
		LinkedList<GameObject> objects = new LinkedList<GameObject>();
		Vector axis1 = rot.unit().scale(radius);
		Vector axis2 = axis1.cross(Vector.UNIT_Y);
		for (GameObject o : gameObjects) {
			Vector deltaPos = o.getPos().sub(center);
			if (deltaPos.proj(axis1).mag() <= radius && deltaPos.proj(axis2).mag() <= radius
					&& deltaPos.proj(Vector.UNIT_Y).mag() <= radius) {
				if (matchesFilterTags(o, filterTags)) {
					objects.add(o);
				}
			}
		}
		return objects;
	}

	public static LinkedList<GameObject> objectsInLine(Vector origin, Vector rot, double length, double radius,
			String[] filterTags) {
		LinkedList<GameObject> objects = new LinkedList<GameObject>();
		for (GameObject o : gameObjects) {
			Vector deltaPos = o.getPos().sub(origin);
			if (deltaPos.cross(rot).mag() / rot.mag() <= radius
					&& deltaPos.calculateAngleDiff(rot) <= Math.toRadians(90) && deltaPos.proj(rot).mag() <= length) {
				if (matchesFilterTags(o, filterTags)) {
					objects.add(o);
				}
			}
		}
		return objects;
	}

	public static LinkedList<GameObject> objectsInSphere(Vector center, double radius, String[] filterTags) {
		LinkedList<GameObject> objects = new LinkedList<GameObject>();
		for (GameObject o : gameObjects) {
			if (o.getPos().sub(center).mag() <= radius) {
				if (matchesFilterTags(o, filterTags)) {
					objects.add(o);
				}
			}
		}
		return objects;
	}

	public static LinkedList<GameObject> getGameObjects() {
		return gameObjects;
	}

	private static boolean matchesFilterTags(GameObject o, String[] filterTags) {
		boolean valid = true;
		for (String tag : filterTags) {
//			if (!o.hasTag(tag)) {
//				valid = false;
//				break;
//			}
		}
		return valid;
	}

}
