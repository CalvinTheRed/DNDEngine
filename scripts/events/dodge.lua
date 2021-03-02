function invokeEvent()
	effect = luajava.newInstance("dnd.effects.Effect", "scripts/effects/dodge.lua", "Dodge", source, target)
	target:addEffect(effect)
end