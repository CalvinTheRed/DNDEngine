function define()
	Item = luajava.bindClass("com.dndsuite.core.Item")
	
	self:setName("Heavy Crossbow")
	self:addTag(Item.HEAVY)
	self:addTag(Item.LOADING)
	self:addTag(Item.RANGE)
	self:addTag(Item.TWO_HANDED)
	self:addTag(Item.MARTIAL_RANGED)
	self:addTag(Item.HEAVY_CROSSBOW)
end



function acAbilityBonusLimit()
	return 0
end



function acbase()
	return 0
end



function customTasks()
end



function damage()
	AttackRoll = luajava.bindClass("com.dndsuite.core.events.contests.AttackRoll")
	DamageType = luajava.bindClass("com.dndsuite.dnd.data.DamageType")
	
	if attackType == AttackRoll.MELEE then
		damageDice = luajava.newInstance("com.dndsuite.dnd.combat.DamageDiceGroup", 1, 4, DamageType.BLUDGEONING)
		return damageDice
	elseif attackType == AttackRoll.RANGED then
		damageDice = luajava.newInstance("com.dndsuite.dnd.combat.DamageDiceGroup", 1, 10, DamageType.PIERCING)
		return damageDice
	elseif attackType == AttackRoll.THROWN then
		damageDice = luajava.newInstance("com.dndsuite.dnd.combat.DamageDiceGroup", 1, 4, DamageType.BLUDGEONING)
		return damageDice
	end
	
end



function equip()
end



function range()
	return 20.0, 40.0, 100.0, 400.0
end



function reach()
	return 0.0
end



function unequip()
end



function weight()
	return 18.0
end