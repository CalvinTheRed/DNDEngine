package main;

import dnd.effects.Evasion;
import dnd.events.dicecontests.savingthrow.Fireball;
import gameobjects.entities.Entity;
import gameobjects.entities.Zombie;
import maths.Vector;

public class Main {
	
	//public static Window window;
	
	public static void main(String[] args) {
		//initialize();
		test();
	}
	
	private static void initialize() {
		//Window.setDefaultLookAndFeelDecorated(false);
		//window = new Window();
	}
	
	private static void test() {
		
		Entity zombie1 = new Zombie(new Vector(30, 0, 0), new Vector(1, 0, 0));
		
		Entity zombie2 = new Zombie(new Vector(0, 0, 1), new Vector(1, 0, 0));
		Entity zombie3 = new Zombie(new Vector(0, 0, 2), new Vector(1, 0, 0));
		Entity zombie4 = new Zombie(new Vector(1, 0, 0), new Vector(1, 0, 0));
		Entity zombie5 = new Zombie(new Vector(1, 0, 1), new Vector(1, 0, 0));
		Entity zombie6 = new Zombie(new Vector(1, 0, 2), new Vector(1, 0, 0));
		
		zombie4.invokeTask(0);
		zombie4.invokeQueuedEvent(0, 0, zombie2.getPos());
		zombie4.addEffect(new Evasion(zombie4, zombie4));
		
		Fireball fb = new Fireball(Entity.WIS, 3);
		fb.invoke(zombie1, zombie2.getPos());
		
		
//		zombie1.invokeTask(0);
//		zombie1.invokeTask(2);
//		System.out.println();
//		
//		System.out.println(zombie1 + "'s queued events:");
//		for (EventGroup group : zombie1.getEventQueue()) {
//			System.out.println(group);
//		}
//		System.out.println();
//		
//		zombie1.invokeQueuedEvent(0, 0, zombie1.getPos());
//		System.out.println();
//		
//		System.out.println(zombie1 + "'s queued events:");
//		for (EventGroup group : zombie1.getEventQueue()) {
//			System.out.println(group);
//		}
//		System.out.println();
//		
//		zombie1.invokeQueuedEvent(0, 0, zombie1.getPos());
//		System.out.println();
//		
//		System.out.println(zombie1 + "'s queued events:");
//		for (EventGroup group : zombie1.getEventQueue()) {
//			System.out.println(group);
//		}
//		System.out.println();
	}
	 
}
