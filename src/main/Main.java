package main;

import engine.Manager;
import engine.effects.Effect;
import engine.effects.FightingStyleDueling;
import engine.effects.GuidingBoltEffect;
import engine.io.Window;
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
		
		// TODO: verify Damage events can be processed and modified by effects
		
		Entity zombie1 = new Zombie(new Vector(0, 0, 0), new Vector(0, 0, 0));
		Entity zombie2 = new Zombie(new Vector(0, 0, 0), new Vector(0, 0, 0));
		Entity zombie3 = new Zombie(new Vector(0, 0, 0), new Vector(0, 0, 0));
		Entity zombie4 = new Zombie(new Vector(0, 0, 0), new Vector(0, 0, 0));
		Manager.addGameObject(zombie1);
		Manager.addGameObject(zombie2);
		Manager.addGameObject(zombie3);
		Manager.addGameObject(zombie4);
		zombie1.addTarget(zombie2);
		zombie2.addTarget(zombie1);
		
		zombie1.observeEffect(new FightingStyleDueling(zombie1, zombie1));
		System.out.println();
		zombie2.observeEffect(new GuidingBoltEffect(zombie1, zombie2));
		System.out.println();
		
		System.out.println("Effects on Zombie2:");
		for (Effect e : zombie2.getObservedEffects()) {
			System.out.println(e + " - " + (e.isEnded() ? "ended" : "active"));
		}
		System.out.println();
		
		zombie1.invokeEvent(zombie1.getInvokableEvents().get(0));
		System.out.println();
		
		System.out.println("Effects on Zombie2:");
		for (Effect e : zombie2.getObservedEffects()) {
			System.out.println(e + " - " + (e.isEnded() ? "ended" : "active"));
		}
		System.out.println();
	}
	 
}
