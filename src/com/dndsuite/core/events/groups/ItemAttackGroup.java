package com.dndsuite.core.events.groups;

import com.dndsuite.core.Item;
import com.dndsuite.core.Observer;
import com.dndsuite.core.Subject;
import com.dndsuite.core.events.ItemAttack;
import com.dndsuite.core.gameobjects.Entity;

public class ItemAttackGroup extends EventGroup implements Observer {
	public static boolean MAINHAND = true;
	public static boolean OFFHAND = false;
	
	protected boolean mainhand;
	
	public ItemAttackGroup(boolean mainhand) {
		super();
		this.mainhand = mainhand;
	}

	@Override
	public void update(Subject s) {
		Entity subject = (Entity) s;
		Item item;
		if (mainhand) {
			item = subject.getMainhand();
		} else {
			item = subject.getOffhand();
		} 
		if (item == null) {
			events.clear();
			events.add(new ItemAttack(Entity.STR, null, ItemAttack.MELEE));
		} else {
			events = item.getItemAttackOptions();
			
		}
	}

}
