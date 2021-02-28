function invokeTask()

group = luajava.newInstance("dnd.events.eventgroups.EventGroup")
event = luajava.newInstance("dnd.events.Event", "scripts/events/dodge.lua", "Dodge")
group:addEvent(event)
invoker:queueEventGroup(group)

end



function getTaskCost()

return "Free"

end