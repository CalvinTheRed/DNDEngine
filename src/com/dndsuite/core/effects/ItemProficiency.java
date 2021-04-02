package com.dndsuite.core.effects;

import com.dndsuite.core.Item;
import com.dndsuite.core.events.Event;
import com.dndsuite.core.events.ItemAttack;
import com.dndsuite.core.events.contests.AttackRoll;
import com.dndsuite.core.gameobjects.Entity;
import com.dndsuite.core.gameobjects.GameObject;

public class ItemProficiency extends Effect {
	protected String itemTag;
	
	public ItemProficiency(Entity source, Entity target, String itemTag) {
		super(null, source, target);
		this.itemTag = itemTag;
		setName(ItemProficiency.getEffectID() + " (" + itemTag + ")");
		addTag(ItemProficiency.getEffectID());
	}
	
	public static String getEffectID() {
		return "Item Proficiency";
	}
	
	public String getItemTag() {
		return itemTag;
	}
	
	@Override
	public void processEventSafe(Event e, Entity source, GameObject target) {
		// This function assumes that the source of the event
		// is the one whose proficiency bonus could apply
		if (e instanceof AttackRoll && source == getTarget()) {
			AttackRoll ar = (AttackRoll) e;
			if (ar.getParent() instanceof ItemAttack) {
				ItemAttack parent = (ItemAttack) ar.getParent();
				if (parent.getMedium().hasTag(itemTag) && parent.getMedium().hasTag(Item.WEAPON)) {
					try {
						e.applyEffect(this);
						ar.addBonus(source.getProficiencyBonus());
					} catch (Exception ex) {
						System.out.println("[JAVA] Effect " + this + " already applied to event " + e);
					}
				}
			}
		}
	}

}
