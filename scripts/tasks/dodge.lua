function invokeTask()
	Event = luajava.bindClass("dnd.events.Event")
	group = luajava.newInstance("dnd.events.eventgroups.EventGroup")
	event = luajava.newInstance("dnd.events.Event", "scripts/events/dodge.lua", "Dodge", Event.SINGLE_TARGET)
	group:addEvent(event)
	invoker:queueEventGroup(group)
end



function getTaskCost()
	return "Free"
end