package testing.dnd.effects;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import core.gameobjects.Entity;

// TODO: remove once a better example of parameter resolver is implemented
public class DummyEffectParameterResolver implements ParameterResolver {

	public static Entity Source;
	public static Entity Target;

	@Override
	public Object resolveParameter(ParameterContext arg0, ExtensionContext arg1) throws ParameterResolutionException {
		if (arg0.getParameter().getType() == DummyEffect.class) {
			// TODO: does priority interfere with this tester?
			return new DummyEffect(Source, Target);
		}
		return null;
	}

	@Override
	public boolean supportsParameter(ParameterContext arg0, ExtensionContext arg1) throws ParameterResolutionException {
		if (arg0.getParameter().getType() == DummyEffect.class) {
			return true;
		}
		return false;
	}

}
