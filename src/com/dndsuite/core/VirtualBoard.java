package com.dndsuite.core;

import java.util.ArrayList;

import org.json.simple.JSONObject;

import com.dndsuite.exceptions.InvalidAreaOfEffectException;
import com.dndsuite.exceptions.OutOfRangeException;
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

	private static ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
	// TODO: consider adding queuedGlobalEvents list here

	public static boolean add(GameObject o) {
		if (gameObjects.contains(o)) {
			return false;
		}
		gameObjects.add(o);
		return true;
	}

	public static boolean remove(GameObject o) {
		return gameObjects.remove(o);
	}

	public static void clear() {
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
					if (!o.hasTag(tag)) {
						valid = false;
						break;
					}
				}
				if (valid) {
					nearest = o;
					distance = nearest.getPos().sub(pos).mag();
				}
			}
		}
		return nearest;
	}

	public static ArrayList<GameObject> objectsInCone(Vector vertex, Vector pointTo, double length, String[] filterTags) {
		Vector rot = pointTo.sub(vertex);
		ArrayList<GameObject> objects = new ArrayList<GameObject>();
		for (GameObject o : gameObjects) {
			Vector deltaPos = o.getPos().sub(vertex);
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

	public static ArrayList<GameObject> objectsInCube(Vector center, Vector pointTo, double radius, String[] filterTags) {
		Vector rot = pointTo.sub(center);
		ArrayList<GameObject> objects = new ArrayList<GameObject>();
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

	public static ArrayList<GameObject> objectsInLine(Vector start, Vector pointTo, double length, double radius,
			String[] filterTags) {
		Vector rot = pointTo.sub(start);
		ArrayList<GameObject> objects = new ArrayList<GameObject>();
		for (GameObject o : gameObjects) {
			Vector deltaPos = o.getPos().sub(start);
			if (deltaPos.cross(rot).mag() / rot.mag() <= radius
					&& deltaPos.calculateAngleDiff(rot) <= Math.toRadians(90) && deltaPos.proj(rot).mag() <= length) {
				if (matchesFilterTags(o, filterTags)) {
					objects.add(o);
				}
			}
		}
		return objects;
	}

	public static ArrayList<GameObject> objectsInSphere(Vector center, double radius, String[] filterTags) {
		ArrayList<GameObject> objects = new ArrayList<GameObject>();
		for (GameObject o : gameObjects) {
			if (o.getPos().sub(center).mag() <= radius) {
				if (matchesFilterTags(o, filterTags)) {
					objects.add(o);
				}
			}
		}
		return objects;
	}

	public static ArrayList<GameObject> objectsInAreaOfEffect(Vector sourcePos, Vector startPos, Vector pointTo,
			JSONObject json) throws InvalidAreaOfEffectException, OutOfRangeException {
		ArrayList<GameObject> objects = new ArrayList<GameObject>();

		JSONObject areaOfEffect = (JSONObject) json.get("area_of_effect");
		String shape = (String) areaOfEffect.get("shape");

		if (shape.equals("single_target")) {
			if (areaOfEffect.get("range").equals("self")) {
				objects.add(nearestObject(sourcePos, new String[] {}));
			} else if (areaOfEffect.get("range") instanceof JSONObject) {
				JSONObject range = (JSONObject) areaOfEffect.get("range");
				if (startPos.sub(sourcePos).mag() <= (double) range.get("long")) {
					objects.add(nearestObject(startPos, new String[] {}));
				} else {
					throw new OutOfRangeException((double) range.get("long"), startPos.sub(sourcePos).mag());
				}
			} else if (areaOfEffect.get("range") instanceof Double) {
				if (startPos.sub(sourcePos).mag() <= (double) areaOfEffect.get("range")) {
					objects.add(nearestObject(startPos, new String[] {}));
				} else {
					throw new OutOfRangeException((double) areaOfEffect.get("range"), startPos.sub(sourcePos).mag());
				}
			} else {
				throw new InvalidAreaOfEffectException();
			}

		} else if (shape.equals("cone")) {
			if (areaOfEffect.get("range").equals("self")) {
				objects.addAll(objectsInCone(sourcePos, pointTo, (double) areaOfEffect.get("length"), new String[] {}));
			} else if (areaOfEffect.get("range") instanceof Double) {
				if (startPos.sub(sourcePos).mag() <= (double) areaOfEffect.get("range")) {
					objects.addAll(
							objectsInCone(startPos, pointTo, (double) areaOfEffect.get("length"), new String[] {}));
				} else {
					throw new OutOfRangeException((double) areaOfEffect.get("range"), startPos.sub(sourcePos).mag());
				}
			} else {
				throw new InvalidAreaOfEffectException();
			}

		} else if (shape.equals("cube")) {
			if (areaOfEffect.get("range").equals("self")) {
				objects.addAll(objectsInCube(sourcePos, pointTo, (double) areaOfEffect.get("radius"), new String[] {}));
			} else if (areaOfEffect.get("range").equals("adjacent")) {
				// Note that adjacent cubes always have the sourcePos centered along one of its
				// faces
				Vector delta = pointTo.sub(sourcePos).unit().scale((double) areaOfEffect.get("radius"));
				objects.addAll(objectsInCube(sourcePos.add(delta), pointTo, (double) areaOfEffect.get("radius"),
						new String[] {}));
			} else if (areaOfEffect.get("range") instanceof Double) {
				if (startPos.sub(sourcePos).mag() <= (double) areaOfEffect.get("range")) {
					objects.addAll(
							objectsInCube(startPos, pointTo, (double) areaOfEffect.get("radius"), new String[] {}));
				} else {
					throw new OutOfRangeException((double) areaOfEffect.get("range"), startPos.sub(sourcePos).mag());
				}
			} else {
				throw new InvalidAreaOfEffectException();
			}

		} else if (shape.equals("line")) {
			if (areaOfEffect.get("range").equals("self")) {
				objects.addAll(objectsInLine(sourcePos, pointTo, (double) areaOfEffect.get("length"),
						(double) areaOfEffect.get("radius"), new String[] {}));
			} else if (areaOfEffect.get("range") instanceof Double) {
				if (startPos.sub(sourcePos).mag() <= (double) areaOfEffect.get("range")) {
					objects.addAll(objectsInLine(startPos, pointTo, (double) areaOfEffect.get("length"),
							(double) areaOfEffect.get("radius"), new String[] {}));
				} else {
					throw new OutOfRangeException((double) areaOfEffect.get("range"), startPos.sub(sourcePos).mag());
				}
			} else {
				throw new InvalidAreaOfEffectException();
			}

		} else if (shape.equals("sphere")) {
			if (areaOfEffect.get("range").equals("self")) {
				objects.addAll(objectsInSphere(sourcePos, (double) areaOfEffect.get("radius"), new String[] {}));
			} else if (areaOfEffect.get("range") instanceof Double) {
				if (startPos.sub(sourcePos).mag() <= (double) areaOfEffect.get("range")) {
					objects.addAll(objectsInSphere(startPos, (double) areaOfEffect.get("radius"), new String[] {}));
				} else {
					throw new OutOfRangeException((double) areaOfEffect.get("range"), startPos.sub(sourcePos).mag());
				}
			} else {
				throw new InvalidAreaOfEffectException();
			}
		} else {
			throw new InvalidAreaOfEffectException();
		}

		// TODO: add a filter for disregarding the source from the area of effect

		return objects;
	}

	public static ArrayList<GameObject> getGameObjects() {
		return gameObjects;
	}

	private static boolean matchesFilterTags(GameObject o, String[] filterTags) {
		boolean valid = true;
		for (String tag : filterTags) {
			if (!o.hasTag(tag)) {
				valid = false;
				break;
			}
		}
		return valid;
	}

}
