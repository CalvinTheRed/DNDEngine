function define()
	Item = luajava.bindClass("core.Item")
	
	item:setName("Test Item")
	item:addTag(Item.FINESSE)
	item:addTag(Item.LIGHT)
	item:addTag(Item.RANGE)
	item:addTag(Item.THROWN)
	item:addTag(Item.MARTIAL_RANGED)
	item:addTag(Item.ARMOR)
	item:addTag(Item.SHIELD)
end



function acAbilityBonusLimit()
	return 2
end



function acbase()
	return 15
end



function customTasks()
	task = luajava.newInstance("core.tasks.Task", "scripts/tasks/dodge.lua")
	return task
end



function damage()
	ar = luajava.bindClass("core.events.AttackRoll")
	dt = luajava.bindClass("dnd.data.DamageType")
	
	if attackType == ar.MELEE then
		damageDice = luajava.newInstance("dnd.combat.DamageDiceGroup", 1, 1, dt.BLUDGEONING)
		return damageDice
	elseif attackType == ar.RANGED then
		damageDice = luajava.newInstance("dnd.combat.DamageDiceGroup", 1, 1, dt.PIERCING)
		return damageDice
	elseif attackType == ar.THROWN then
		damageDice = luajava.newInstance("dnd.combat.DamageDiceGroup", 1, 1, dt.BLUDGEONING)
		return damageDice
	end
	
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