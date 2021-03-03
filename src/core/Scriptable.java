package core;

import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.jse.JsePlatform;

public abstract class Scriptable {
	protected Globals globals;
	protected String script;

	public Scriptable(String script) {
		this.script = script;
		if (script != null) {
			globals = JsePlatform.standardGlobals();
			globals.loadfile(script).call();
		}
	}

}
