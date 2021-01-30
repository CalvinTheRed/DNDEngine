package engine;

import java.util.LinkedList;

import engine.effects.Effect;
import engine.events.Event;
import gameobjects.GameObject;
import gameobjects.entities.Entity;
import maths.Vector;

public final class Manager {
	
	private static final double CONE_ARC_SIZE = Math.toRadians(45);
	
	private static LinkedList<GameObject> gameObjects;
	private static LinkedList<Effect> effects;
	
	public static void initialize() {
		gameObjects = new LinkedList<GameObject>();
		effects = new LinkedList<Effect>();
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
	
	public static void manageGameObjects() {
		for (GameObject o : gameObjects) {
			o.manage();
		}
	}
	
	public static boolean addEffect(Effect e) {
		if (effects.contains(e)) {
			return false;
		}
		effects.add(e);
		return true;
	}
	
	// TODO: implement the utility of Effect.expire()
	public static boolean removeEffect(Effect e) {
		for (Effect effect : effects) {
			if (effect == e) {
				e.expire();
			}
		}
		return effects.remove(e);
	}
	
	public static boolean processEvent(Event e, Entity target) {
		boolean isModified = false;
		for (int i = 0; i < effects.size(); i++) {
			/* avoid ConcurrentModificationException by using this
			 * form of for loop, and account for if the event is
			 * deleted upon being processed
			 */
			Effect effect = effects.get(i);
			if (effect.processEvent(e, target)) {
				isModified = true;
			}
			if (i < effects.size() && effect != effects.get(i)) {
				i--;
			}
		}
		return isModified;
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
				if (deltaPos.proj(axis1).mag() <= radius && deltaPos.proj(axis2).mag() <= radius && deltaPos.proj(Vector.UNIT_Y).mag() <= radius * 2) {
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
