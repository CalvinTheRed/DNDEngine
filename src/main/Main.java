package main;

import dnd.events.EventGroup;
import engine.Manager;
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
		
		Entity zombie1 = new Zombie(new Vector(0, 0, -0.01), new Vector(0, 0, 0));
		Entity zombie2 = new Zombie(new Vector(0, 0, 4), new Vector(0, 0, 0));
		Entity zombie3 = new Zombie(new Vector(0, 0, 5), new Vector(0, 0, 0));
		Entity zombie4 = new Zombie(new Vector(0, 0, 30), new Vector(0, 0, 0));
		Manager.addGameObject(zombie1);
		Manager.addGameObject(zombie2);
		Manager.addGameObject(zombie3);
		Manager.addGameObject(zombie4);
		
		zombie1.invokeTask(0);
		zombie1.invokeTask(1);
		
		System.out.println(zombie1 + "'s queued events:");
		for (EventGroup group : zombie1.getEventQueue()) {
			System.out.println(group);
		}
		System.out.println();
		
		zombie1.invokeQueuedEvent(0, 0, zombie1.getPos());
		System.out.println();
		
		System.out.println(zombie1 + "'s queued events:");
		for (EventGroup group : zombie1.getEventQueue()) {
			System.out.println(group);
		}
		
	}
	 
}
