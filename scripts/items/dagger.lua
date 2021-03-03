function define()
	Item = luajava.bindClass("core.Item")
	
	item:setName("Dagger")
	item:addTag(Item.FINESSE)
	item:addTag(Item.LIGHT)
	item:addTag(Item.THROWN)
	item:addTag(Item.SIMPLE_MELEE)
	item:addTag(Item.DAGGER)
end



function damageDice()
	dt = luajava.bindClass("dnd.data.DamageType")
	damageDice = luajava.newInstance("dnd.combat.DamageDiceGroup", 1, 4, dt.PIERCING)
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