function define()
	Item = luajava.bindClass("com.dndsuite.core.Item")
	
	self:setName("Club")
	self:addTag(Item.LIGHT)
	self:addTag(Item.SIMPLE_MELEE)
	self:addTag(Item.CLUB)
end



function acAbilityBonusLimit()
	return 0
end



function acbase()
	return 0
end



function customTasks()
	task = luajava.newInstance("com.dndsuite.core.tasks.Task", "scripts/tasks/dodge.lua")
	return task
end



function damage()
	AttackRoll = luajava.bindClass("com.dndsuite.core.events.contests.AttackRoll")
	DamageType = luajava.bindClass("com.dndsuite.dnd.data.DamageType")
	
	if attackType == AttackRoll.MELEE then
		damageDice = luajava.newInstance("com.dndsuite.dnd.combat.DamageDiceGroup", 1, 4, DamageType.BLUDGEONING)
		return damageDice
	elseif attackType == AttackRoll.RANGED then
		return nil
	elseif attackType == AttackRoll.THROWN then
		damageDice = luajava.newInstance("com.dndsuite.dnd.combat.DamageDiceGroup", 1, 4, DamageType.BLUDGEONING)
		return damageDice
	end
	
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