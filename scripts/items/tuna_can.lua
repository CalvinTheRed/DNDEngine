function define()
	Item = luajava.bindClass("core.Item")
	
	item:setName("Can of tuna")
	item:addTag(Item.LIGHT)
	item:addTag(Item.SIMPLE_RANGED)
	item:addTag(Item.RANGED)
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
		damageDice = luajava.newInstance("dnd.combat.DamageDiceGroup", 1, 4, dt.BLUDGEONING)
		return damageDice
	elseif attackType == ar.RANGED then
		damageDice = luajava.newInstance("dnd.combat.DamageDiceGroup", 1, 10, dt.PSYCHIC)
		return damageDice
	elseif attackType == ar.THROWN then
		damageDice = luajava.newInstance("dnd.combat.DamageDiceGroup", 6, 10, dt.SLASHING)
		return damageDice
	end
	
end



function equip()
	effect = luajava.newInstance("core.effects.Effect", "scripts/effects/cat_swarm.lua", subject, subject)
	subject:addEffect(effect) 
end



function range()
	return 12.0, 20.0, 6.0, 9.0
end



function reach()
	return 0.0
end



function unequip()
end



function weight()
	return 1.0
end