function define()
	Item = luajava.bindClass("com.dndsuite.core.Item")
	
	self:setName("Studded Leather Armor")
	self:addTag(Item.ARMOR)
	self:addTag(Item.ARMOR_LIGHT)
end



function acAbilityBonusLimit()
	return -1
end



function acbase()
	return 12
end



function customTasks()
end



function damage()
	DamageType = luajava.bindClass("com.dndsuite.dnd.data.DamageType")
	
	damageDice = luajava.newInstance("com.dndsuite.dnd.combat.DamageDiceGroup", 1, 4, DamageType.BLUDGEONING)
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
	return 13.0
end