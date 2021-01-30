package main;

import engine.Manager;
import engine.io.Window;
import gameobjects.entities.Entity;
import gameobjects.entities.Zombie;
import maths.Vector;

public class Main {
	
	public static Window window;
	
	public static void main(String[] args) {
		initialize();
		test();
	}
	
	private static void initialize(){
		Manager.initialize();
		Window.setDefaultLookAndFeelDecorated(false);
		//window = new Window();
	}
	
	public static void test() {
//		int width = 25;
//		int depth = 25;
//		for (int i = 0; i < width; i++) {
//			for (int j = 0; j < depth; j++) {
//				Manager.addGameObject(new Zombie(new Vector(-(width-1)/2 + i, 0, -(depth-1)/2 + j), new Vector (0, 0, 1)));
//			}
//		}
//		
//		Manager.entitiesInCube(new Vector(3, 0, 0), new Vector(1, 0, 0), 2);
		
		Entity zombie1 = new Zombie(new Vector(0, 0, 0), new Vector(0, 0, 0));
		Entity zombie2 = new Zombie(new Vector(5, 0, 0), new Vector(0, 0, 0));
		Entity zombie3 = new Zombie(new Vector(5, 0, 0), new Vector(0, 0, 0));
		Entity zombie4 = new Zombie(new Vector(5, 0, 0), new Vector(0, 0, 0));
		Manager.addGameObject(zombie1);
		Manager.addGameObject(zombie2);
		Manager.addGameObject(zombie3);
		Manager.addGameObject(zombie4);
		zombie1.addTarget(zombie2);
		zombie1.addTarget(zombie3);
		zombie1.addTarget(zombie4);
		
		zombie1.invokeEvent(zombie1.getAvailableEvents().get(1));
		System.out.println();
		
	}
	
}
