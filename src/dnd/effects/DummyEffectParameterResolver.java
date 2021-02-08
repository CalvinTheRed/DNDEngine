package dnd.effects;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import gameobjects.entities.DummyEntity;
import gameobjects.entities.Entity;
import maths.Vector;

public class DummyEffectParameterResolver implements ParameterResolver {

	public static final Entity Source = new DummyEntity("Dummy Entity Source", new Vector(0,0,0), new Vector(1,1,1) );
	public static final Entity Target = new DummyEntity("Dummy Entity Target", new Vector(1,0,0), new Vector(1,0,0) );
	
	@Override
	public Object resolveParameter(ParameterContext arg0, ExtensionContext arg1) throws ParameterResolutionException {
		if (arg0.getParameter().getType()== DummyEffect.class) {
			return new DummyEffect(Source, Target, "Dummy Effect");
		}
		return null;
	}

	@Override
	public boolean supportsParameter(ParameterContext arg0, ExtensionContext arg1) throws ParameterResolutionException {
		if (arg0.getParameter().getType()== DummyEffect.class) {
			return true;
		}
		return false;
	}

}
