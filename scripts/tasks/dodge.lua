function define()
	self:setName("Dodge")
end



function invokeTask()
	Event = luajava.bindClass("com.dndsuite.core.events.Event")
	group = luajava.newInstance("com.dndsuite.core.events.groups.EventGroup")
	event = luajava.newInstance("com.dndsuite.core.events.Event", "scripts/events/dodge.lua")
	group:addEvent(event)
	invoker:queueEventGroup(group)
end



function getTaskCost()
	return "Free"
end