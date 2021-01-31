package gameobjects.entities;

import maths.Vector;

public class TestDummy extends Entity {

	public TestDummy() {
		super("Dummy", new Vector(0,0,0), new Vector(0,0,0));
	}
	
	public TestDummy(String name, Vector pos, Vector rot) {
		super(name, pos, rot);
	}

	@Override
	public void manage() {
		// TODO Auto-generated method stub

	}

}
