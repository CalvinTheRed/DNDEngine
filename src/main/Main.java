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
	
	private static void test() {
		
		Entity zombie1 = new Zombie(new Vector(0, 0, 0), new Vector(0, 0, 0));
		Entity zombie2 = new Zombie(new Vector(0, 0, 0), new Vector(0, 0, 0));
		Entity zombie3 = new Zombie(new Vector(0, 0, 0), new Vector(0, 0, 0));
		Entity zombie4 = new Zombie(new Vector(0, 0, 0), new Vector(0, 0, 0));
		Manager.addGameObject(zombie1);
		Manager.addGameObject(zombie2);
		Manager.addGameObject(zombie3);
		Manager.addGameObject(zombie4);
		
		zombie1.addTarget(zombie2);
		//zombie1.addTarget(zombie3);
		//zombie1.addTarget(zombie4);
		
		zombie2.addTarget(zombie1);
		
		// attacks against zombie2 will have advantage
		zombie2.observeEffect(new GuidingBoltEffect(zombie1, zombie2));
		System.out.println();
		// attacks made by zombie1 will have disadvantage
		zombie1.observeEffect(new ViciousMockeryEffect(zombie2, zombie1));
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
		
		zombie1.invokeEvent(zombie1.getAvailableEvents().get(0));
		//zombie1.invokeEvent(zombie1.getAvailableEvents().get(1));
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
