function define()
	DamageDiceGroup = luajava.bindClass("com.dndsuite.dnd.combat.DamageDiceGroup")
	DamageType = luajava.bindClass("com.dndsuite.dnd.data.DamageType")
	
	self:setName("Dummy Item")
end



function acAbilityBonusLimit()
	return -1.0
end



function acbase()
	return 10
end



function customTasks()
end



function damage()
	damageDice = luajava.newInstance("com.dndsuite.dnd.combat.DamageDiceGroup", 1, 1, DamageType.BLUDGEONING)
	return damageDice;
end



function equip()
end



function range()
	return 20.0, 40.0, 20.0, 40.0
end



function reach()
	return 0.0
end



function unequip()
end



function weight()
	return 1.0
end