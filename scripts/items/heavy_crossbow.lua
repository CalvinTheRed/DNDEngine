function define()
	Item = luajava.bindClass("core.Item")
	
	item:setName("Heavy Crossbow")
	item:addTag(Item.HEAVY)
	item:addTag(Item.LOADING)
	item:addTag(Item.RANGE)
	item:addTag(Item.TWO_HANDED)
	item:addTag(Item.MARTIAL_RANGED)
	item:addTag(Item.HEAVY_CROSSBOW)
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
	ar = luajava.bindClass("core.events.contests.AttackRoll")
	dt = luajava.bindClass("dnd.data.DamageType")
	
	if attackType == ar.MELEE then
		damageDice = luajava.newInstance("dnd.combat.DamageDiceGroup", 1, 4, dt.BLUDGEONING)
		return damageDice
	elseif attackType == ar.RANGED then
		damageDice = luajava.newInstance("dnd.combat.DamageDiceGroup", 1, 10, dt.PIERCING)
		return damageDice
	elseif attackType == ar.THROWN then
		damageDice = luajava.newInstance("dnd.combat.DamageDiceGroup", 1, 4, dt.BLUDGEONING)
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