function define()
	Item = luajava.bindClass("core.Item")
	
	item:setName("Dagger")
	item:addTag(Item.FINESSE)
	item:addTag(Item.LIGHT)
	item:addTag(Item.THROWN)
	item:addTag(Item.SIMPLE_MELEE)
	item:addTag(Item.DAGGER)
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
	ar = luajava.bindClass("core.events.AttackRoll")
	dt = luajava.bindClass("dnd.data.DamageType")
	
	if attackType == ar.MELEE then
		damageDice = luajava.newInstance("dnd.combat.DamageDiceGroup", 1, 4, dt.PIERCING)
		return damageDice
	elseif attackType == ar.RANGED then
		return nil
	elseif attackType == ar.THROWN then
		damageDice = luajava.newInstance("dnd.combat.DamageDiceGroup", 1, 4, dt.PIERCING)
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
	return 1.0
end