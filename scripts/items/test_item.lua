function define()
	Item = luajava.bindClass("core.Item")
	
	item:setName("Test Item")
	item:addTag(Item.FINESSE)
	item:addTag(Item.LIGHT)
	item:addTag(Item.RANGE)
	item:addTag(Item.THROWN)
	item:addTag(Item.MARTIAL_RANGED)
end



function customTasks()
	task = luajava.newInstance("core.tasks.Task", "scripts/tasks/dodge.lua")
	return task
end



function damage()
	dt = luajava.bindClass("dnd.data.DamageType")
	damageDice = luajava.newInstance("dnd.combat.DamageDiceGroup", 1, 4, dt.SLASHING)
	return damageDice
end



function equip()
end



function range()
	return 20.0, 40.0, 60.0, 180.0
end



function reach()
	return 5.0
end



function unequip()
end



function weight()
	return 10.0
end