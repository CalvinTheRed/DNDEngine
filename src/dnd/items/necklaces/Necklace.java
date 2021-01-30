package dnd.items.necklaces;

import dnd.items.Equippable;
import dnd.items.Item;

public abstract class Necklace extends Item implements Equippable {
	
	public Necklace(String name, int value) {
		super(name, value);
	}
	
}
