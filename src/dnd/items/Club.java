package dnd.items;

import java.util.LinkedList;

import dnd.events.Event;
import dnd.events.dicecontest.MeleeWeaponAttack;
import dnd.events.dicecontest.ThrownWeaponAttack;

public class Club extends Item {
	
	public Club() {
		super("Club");
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public LinkedList<Event> getAttackOptions() {
		LinkedList<Event> events = new LinkedList<Event>();
		Event event1 = new MeleeWeaponAttack(this);
		Event event2 = new ThrownWeaponAttack(this);
		events.add(event1);
		events.add(event2);
		return events;
	}
	
}
