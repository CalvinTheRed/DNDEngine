function define()
	task:setName("Dodge")
end



function invokeTask()
	Event = luajava.bindClass("core.events.Event")
	group = luajava.newInstance("core.events.groups.EventGroup")
	event = luajava.newInstance("core.events.Event", "scripts/events/dodge.lua")
	group:addEvent(event)
	invoker:queueEventGroup(group)
end



function getTaskCost()
	return "Free"
end