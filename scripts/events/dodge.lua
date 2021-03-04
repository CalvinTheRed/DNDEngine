function define()
	Event = luajava.bindClass("core.events.Event")
	event:setName("Dodge")
	event:addTag("Dodge")
	event:addTag(Event.SINGLE_TARGET)
end



function invokeEvent()
	effect = luajava.newInstance("core.Effect", "scripts/effects/dodge.lua", source, target)
	target:addEffect(effect)
end