function invokeTask()
	Event = luajava.bindClass("core.events.Event")
	group = luajava.newInstance("core.events.EventGroup")
	event = luajava.newInstance("core.events.Event", "scripts/events/dodge.lua", "Dodge", Event.SINGLE_TARGET)
	group:addEvent(event)
	invoker:queueEventGroup(group)
end



function getTaskCost()
	return "Free"
end