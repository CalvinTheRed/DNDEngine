package main;

import engine.Manager;
import engine.effects.Effect;
import engine.effects.GuidingBoltEffect;
import engine.effects.ViciousMockeryEffect;
import gameobjects.entities.Entity;
import gameobjects.entities.Zombie;
import maths.Vector;

public class Main {
	
	//public static Window window;
	
	public static void main(String[] args) {
		initialize();
		test();
	}
	
	private static void initialize(){
		Manager.initialize();
		//Window.setDefaultLookAndFeelDecorated(false);
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
		Manager.addGameObject(zombie1);
		Manager.addGameObject(zombie2);
		zombie1.addTarget(zombie2);
		zombie2.addTarget(zombie1);
		
		// attacks against zombie1 will have advantage
		zombie1.observeEffect(new GuidingBoltEffect(zombie2, zombie1));
		System.out.println();
		// attacks made by zombie2 will have disadvatnage
		zombie2.observeEffect(new ViciousMockeryEffect(zombie1, zombie2));
		System.out.println();
		
		System.out.println("Effects on Zombie1:");
		for (Effect e : zombie1.getObservedEffects()) {
			System.out.println(e + " - " + (e.isEnded() ? "ended" : "active"));
		}
		System.out.println();
		
		System.out.println("Effects on Zombie2:");
		for (Effect e : zombie2.getObservedEffects()) {
			System.out.println(e + " - " + (e.isEnded() ? "ended" : "active"));
		}
		System.out.println();
		
		zombie2.invokeEvent(zombie2.getAvailableEvents().get(0));
		System.out.println();
		
		System.out.println("Effects on Zombie1:");
		for (Effect e : zombie1.getObservedEffects()) {
			System.out.println(e + " - " + (e.isEnded() ? "ended" : "active"));
		}
		System.out.println();
		
		System.out.println("Effects on Zombie2:");
		for (Effect e : zombie2.getObservedEffects()) {
			System.out.println(e + " - " + (e.isEnded() ? "ended" : "active"));
		}
		System.out.println();
	}
	
}
