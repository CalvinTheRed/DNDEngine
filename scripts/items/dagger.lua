function define()
	Item = luajava.bindClass("core.Item")
	
	item:setName("Dagger")
	item:addTag(Item.FINESSE)
	item:addTag(Item.LIGHT)
	item:addTag(Item.THROWN)
	item:addTag(Item.SIMPLE_MELEE)
	item:addTag(Item.DAGGER)
end



function customTasks()
end



function damage()
	dt = luajava.bindClass("dnd.data.DamageType")
	damageDice = luajava.newInstance("dnd.combat.DamageDiceGroup", 1, 4, dt.PIERCING)
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
	return 1.0
end