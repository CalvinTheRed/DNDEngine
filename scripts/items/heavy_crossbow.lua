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



function customTasks()
end



function damage()
	dt = luajava.bindClass("dnd.data.DamageType")
	damageDice = luajava.newInstance("dnd.combat.DamageDiceGroup", 1, 10, dt.PIERCING)
	return damageDice
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