package dnd;

import java.util.LinkedList;

import core.gameobjects.Entity;
import core.gameobjects.GameObject;
import maths.Vector;

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

	/**
	 * This function adds a new GameObject object to the virtual board. If the
	 * object is already on the board, it is not added a second time.
	 * 
	 * @param o ({@code GameObject}) object to be added to the virtual board
	 * 
	 * @return {@code boolean} whether o is a novel addition to gameObjects
	 */
	public static boolean addGameObject(GameObject o) {
		if (gameObjects.contains(o)) {
			return false;
		}
		gameObjects.add(o);
		return true;
	}

	/**
	 * This function removes a GameObject object from the virtual board upon which
	 * the game is being played. If the GameObject object is not in the virtual
	 * board, then nothing happens.
	 * 
	 * @param o ({@code GameObject}) object to be removed from the virtual board
	 * 
	 * @return {@code boolean} whether o was present on the virtual board
	 */
	public static boolean removeGameObject(GameObject o) {
		return gameObjects.remove(o);
	}

	/**
	 * This function clears the list of GameObjects that are stored in VirtualBoard.
	 */
	public static void clearGameObjects() {
		gameObjects.clear();
	}

	/**
	 * This function returns whether the VirtualBoard currently contains a
	 * particular GameObject.
	 * 
	 * @param o ({@code GameObject}) the GameObject whose presence is being checked
	 *          for
	 * @return {@code boolean} whether o was contained in the VirtualBoard
	 */
	public static boolean containsGameObject(GameObject o) {
		return gameObjects.contains(o);
	}

	/**
	 * This function returns a single GameObject (Entity) object which is located at
	 * the specified coordinate. If more than one Entity object is located at this
	 * coordinate, this function returns the first object in gameObjects which
	 * matches this coordinate.
	 * 
	 * @param pos ({@code Vector}) the coordinate at which an Entity is being
	 *            searched for
	 * 
	 * @return {@code Entity} an Entity located at the provided position vector
	 */
	public static Entity entityAt(Vector pos) {
		System.out.println("[JAVA] Searching for entity at " + pos);
		for (GameObject o : gameObjects) {
			if (o instanceof Entity && o.getPos().equals(pos)) {
				System.out.println("[JAVA] Found " + o + " " + o.getPos());
				return (Entity) o;
			}
		}
		return null;
	}

	/**
	 * This function returns a list of GameObject (Entity) objects which are located
	 * within the volume of a specified cone. This function does not return an
	 * Entity located at the origin. The Entity objects are filtered from the
	 * corresponding list derived from entitiesInSphere, so this cone has a curved
	 * base rather than a flat base.
	 * 
	 * @deprecated Use VirtualBoard.entitiesInCone2
	 * 
	 * @param origin ({@code Vector}) the coordinate of the vertex/point of the cone
	 * 
	 * @param radius ({@code double}) the length of the cone from vertex to the
	 *               farthest point on the base
	 * 
	 * @param rot    ({@code Vector}) a vector in parallel with the central axis of
	 *               the cone (magnitude does not matter)
	 * 
	 * @return {@code LinkedList<Entity>} a LinkedList of Entity objects which are
	 *         contained in the specified cone
	 */
	public static LinkedList<Entity> entitiesInCone(Vector origin, double radius, Vector rot) {
		LinkedList<Entity> entities = entitiesInSphere(origin, radius);
		System.out.println("[JAVA] Refining search to match cone in direction " + rot.unit() + " within angle "
				+ Math.toDegrees(CONE_ARC_SIZE / 2.0) + " degrees:");
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i).getPos().sub(origin).calculateAngleDiff(rot) > CONE_ARC_SIZE / 2.0
					|| entities.get(i).getPos().equalTo(origin)) {
				entities.remove(i);
				i--;
			} else {
				System.out.println("[JAVA] Found " + entities.get(i) + " " + entities.get(i).getPos());
			}
		}
		return entities;
	}

	/**
	 * This function returns a list of GameObject (Entity) objects which are located
	 * within the volume of a specified cone. This function does not return an
	 * Entity located at the origin.
	 * 
	 * @param origin ({@code Vector}) the coordinate of the vertex/point of the cone
	 * 
	 * @param radius ({@code double}) the length of the cone from vertex to the
	 *               farthest point on the base
	 * 
	 * @param rot    ({@code Vector}) a vector in parallel with the central axis of
	 *               the cone (magnitude does not matter)
	 * 
	 * @return {@code LinkedList<Entity>} a LinkedList of Entity objects which are
	 *         contained in the specified cone
	 */
	public static LinkedList<Entity> entitiesInCone2(Vector origin, Vector rot, double length) {
		System.out.println("[JAVA] Searching for Entities in " + length + "-long cone in direction " + rot.unit()
				+ " from " + origin);
		LinkedList<Entity> entities = new LinkedList<Entity>();
		for (GameObject o : gameObjects) {

			if (o instanceof Entity) {
				Vector deltaPos = o.getPos().sub(origin);
				double radius = Math.sin(CONE_ARC_SIZE / 2) * deltaPos.proj(rot).mag();
				if (deltaPos.cross(rot).mag() / rot.mag() <= radius
						&& deltaPos.calculateAngleDiff(rot) <= Math.toRadians(90)
						&& deltaPos.proj(rot).mag() <= length) {
					System.out.println("[JAVA] Found " + o + " " + o.getPos());
					entities.add((Entity) o);
				}
			}
		}
		return entities;
	}

	/**
	 * This function returns a list of GameObject (Entity) objects which are located
	 * within the volume of a specified cube. The base of the cube in question shall
	 * always be parallel with the X-Z plane.
	 * 
	 * TODO: enforce a parallel base OR re-incorporate rotation vectors
	 * 
	 * @param center ({@code Vector}) the coordinate equally spaced from all
	 *               vertices of the cube
	 * 
	 * @param rot    ({@code Vector}) a vector which is normal to one of the faces
	 *               of the cube
	 * 
	 * @param radius ({@code double}) the distance from the cube's center to one of
	 *               its faces
	 * 
	 * @return {@code LinkedList<Entity>} a LinkedList of Entity objects which are
	 *         contained in the specified cube
	 */
	public static LinkedList<Entity> entitiesInCube(Vector center, Vector rot, double radius) {
		LinkedList<Entity> entities = new LinkedList<Entity>();
		Vector axis1 = rot.unit().scale(radius);
		Vector axis2 = axis1.cross(Vector.UNIT_Y);
		System.out.println(
				"[JAVA] Searching for Entities in cube centered at " + center + " with radius vector " + axis1);
		System.out.println(
				"[JAVA] Defined by base corners " + center.add(axis1).add(axis2) + ", " + center.add(axis1).sub(axis2)
						+ ", " + center.sub(axis1).add(axis2) + ", " + center.sub(axis1).sub(axis2) + ":");
		for (GameObject o : gameObjects) {
			if (o instanceof Entity) {
				Vector deltaPos = o.getPos().sub(center);
				if (deltaPos.proj(axis1).mag() <= radius && deltaPos.proj(axis2).mag() <= radius
						&& deltaPos.proj(Vector.UNIT_Y).mag() <= radius) {
					System.out.println("[JAVA] Found " + o + " " + o.getPos());
					entities.add((Entity) o);
				}
			}
		}
		return entities;
	}

	/**
	 * This function returns a list of GameObject (Entity) objects which are located
	 * within a certain radius of a specified line. Any Entities located precisely
	 * at the origin of this line are not to be included in the list.
	 * 
	 * @param origin ({@code Vector}) the coordinate from which the line emits
	 * @param rot    ({@code Vector}) a vector in parallel with the line
	 * @param length ({@code double}) the length of the line
	 * @param radius ({@code double}) the radius of the line (as though it were a
	 *               cylinder)
	 * @return {@code LinkedList<Entity>} a LinkedList of Entity objects which are
	 *         contained in the volume of the line's area of effect
	 */
	public static LinkedList<Entity> entitiesInLine(Vector origin, Vector rot, double length, double radius) {
		System.out.println("[JAVA] Searching for Entities within " + radius + " of " + length + "-long line " + origin
				+ " + " + rot + "t:");
		LinkedList<Entity> entities = new LinkedList<Entity>();
		for (GameObject o : gameObjects) {

			if (o instanceof Entity) {
				Vector deltaPos = o.getPos().sub(origin);
				if (deltaPos.cross(rot).mag() / rot.mag() <= radius
						&& deltaPos.calculateAngleDiff(rot) <= Math.toRadians(90)
						&& deltaPos.proj(rot).mag() <= length) {
					System.out.println("[JAVA] Found " + o + " " + o.getPos());
					entities.add((Entity) o);
				}
			}
		}
		return entities;
	}

	/**
	 * This function returns a list of GameObject (Entity) objects which are
	 * contained within the volume of a specified sphere.
	 * 
	 * @param center ({@code Vector}) the coordinate of the sphere's center
	 * @param radius ({@code double}) the radius of the sphere
	 * @return {@code LinkedList<Entity>} a LinkedList of Entity objects which are
	 *         within the sphere's volume
	 */
	public static LinkedList<Entity> entitiesInSphere(Vector center, double radius) {
		System.out.println(
				"[JAVA] Searching for Entities in sphere centered at " + center + " with radius " + radius + ":");
		LinkedList<Entity> entities = new LinkedList<Entity>();
		for (GameObject o : gameObjects) {
			if (o instanceof Entity && o.getPos().sub(center).mag() <= radius) {
				System.out.println("[JAVA] Found " + o + " " + o.getPos());
				entities.add((Entity) o);
			}
		}
		return entities;
	}

}