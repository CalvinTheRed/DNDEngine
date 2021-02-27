package engine;

import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.jse.JsePlatform;

public abstract class Scriptable {
	protected Globals globals;
	protected String script;

	public Scriptable(String script) {
		this.script = script;
		globals = JsePlatform.standardGlobals();
		globals.loadfile(script).call();
	}

}
