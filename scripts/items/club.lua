function define()
	Item = luajava.bindClass("core.Item")
	
	item:setName("Club")
	item:addTag(Item.LIGHT)
	item:addTag(Item.SIMPLE_MELEE)
	item:addTag(Item.CLUB)
end



function damageDice()
	dt = luajava.bindClass("dnd.data.DamageType")
	damageDice = luajava.newInstance("dnd.combat.DamageDiceGroup", 1, 4, dt.BLUDGEONING)
	return damageDice
end



function reach()
end



function equip()
end



function unequip()
end



function customTasks()
end