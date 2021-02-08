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
		
		Entity zombie1 = new Zombie(new Vector(0, 0, -0.01), new Vector(0, 0, 1));
		
		zombie1.invokeTask(0);
		zombie1.invokeTask(1);
		
		System.out.println(zombie1 + "'s queued events:");
		for (EventGroup group : zombie1.getEventQueue()) {
			System.out.println(group);
		}
		System.out.println();
		
		zombie1.invokeQueuedEvent(1, 0, zombie1.getPos());
		System.out.println();
		
		System.out.println(zombie1 + "'s queued events:");
		for (EventGroup group : zombie1.getEventQueue()) {
			System.out.println(group);
		}
		
	}
	 
}
