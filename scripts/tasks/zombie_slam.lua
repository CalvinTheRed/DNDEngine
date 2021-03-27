function define()
	Entity = luajava.bindClass("com.dndsuite.core.gameobjects.Entity")
	Event = luajava.bindClass("com.dndsuite.core.events.Event")
	
	self:setName("Slam")
end



function invokeTask()
	local group = luajava.newInstance("com.dndsuite.core.events.groups.EventGroup")
	local event = luajava.newInstance("com.dndsuite.core.events.Event", "scripts/events/contests/zombie_slam.lua", Entity.STR)
	group:addEvent(event)
	invoker:queueEventGroup(group)
end



function getTaskCost()
	return "Free"
end