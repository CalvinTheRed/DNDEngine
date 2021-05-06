function define()
	DamageType = luajava.bindClass("com.dndsuite.dnd.data.DamageType")
	Item = luajava.bindClass("com.dndsuite.core.Item")
	ItemAttack = luajava.bindClass("com.dndsuite.core.events.ItemAttack")
	
	self:setName("Dagger")
	self:addTag(Item.FINESSE)
	self:addTag(Item.LIGHT)
	self:addTag(Item.SIMPLE_MELEE)
	self:addTag(Item.THROWN)
	self:addTag(Item.WEAPON)
	self:addTag(Item.DAGGER)
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
	if attackType == ItemAttack.MELEE then
		return luajava.newInstance("com.dndsuite.dnd.combat.DamageDiceGroup", 1, 4, DamageType.PIERCING)
	elseif attackType == ItemAttack.RANGED then
		return nil
	elseif attackType == ItemAttack.THROWN then
		return luajava.newInstance("com.dndsuite.dnd.combat.DamageDiceGroup", 1, 4, DamageType.PIERCING)
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