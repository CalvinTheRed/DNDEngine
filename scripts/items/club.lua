function define()
	Item = luajava.bindClass("core.Item")
	
	item:setName("Club")
	item:addTag(Item.LIGHT)
	item:addTag(Item.SIMPLE_MELEE)
	item:addTag(Item.CLUB)
end



function customTasks()
	task = luajava.newInstance("core.tasks.Task", "scripts/tasks/dodge.lua")
	return task
end



function damage()
	dt = luajava.bindClass("dnd.data.DamageType")
	damageDice = luajava.newInstance("dnd.combat.DamageDiceGroup", 1, 4, dt.BLUDGEONING)
	return damageDice
end



function equip()
end



function range()
	return 20.0, 40.0, -1.0, -1.0
end



function reach()
	return 0.0
end



function unequip()
end



function weight()
	return 2.0
end