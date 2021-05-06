function define()
	Event = luajava.bindClass("com.dndsuite.core.events.Event")
	self:setName("Dodge")
	self:addTag("Dodge")
	self:addTag(Event.SINGLE_TARGET)
end



function invokeEvent()
	effect = luajava.newInstance("com.dndsuite.core.effects.Effect", "scripts/effects/dodge.lua", source, target)
	target:addEffect(effect)
end