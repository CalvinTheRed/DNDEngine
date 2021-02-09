package main;

import dnd.events.eventgroups.EventGroup;
import dnd.items.Club;
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
		
		Entity zombie1 = new Zombie(new Vector(0, 0, 0), new Vector(1, 0, 0));
		
		zombie1.invokeTask(0);
		zombie1.invokeTask(2);
		zombie1.getInventory().equipWeapon(new Club());
		
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
		System.out.println();
		
		zombie1.invokeQueuedEvent(0, 1, zombie1.getPos());
		System.out.println();
		
		System.out.println(zombie1 + "'s queued events:");
		for (EventGroup group : zombie1.getEventQueue()) {
			System.out.println(group);
		}
		System.out.println();
	}
	 
}
