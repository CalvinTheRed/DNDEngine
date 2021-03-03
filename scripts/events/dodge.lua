function define()
	event:addTag("Dodge")
end



function invokeEvent()
	effect = luajava.newInstance("core.Effect", "scripts/effects/dodge.lua", "Dodge", source, target)
	target:addEffect(effect)
end