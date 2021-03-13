function define()
	Entity = luajava.bindClass("core.gameobjects.Entity")
	Event = luajava.bindClass("core.events.Event")
	
	event:setName("Ice Knife (" .. Entity:getAbility(event:getDCAbility()) .. ", Secondary)")
	event:setSaveAbility(Entity.DEX)
	event:setRange(0.0, 0.0)
	event:setRadius(5.0)
	
	event:addTag(Event.SPHERE)
	event:addTag(Event.SPELL)
	event:addTag("Ice Knife")
end



function additionalEffects()
end



function additionalEffectsOnPass()
end



function damage()
	DamageType = luajava.bindClass("dnd.data.DamageType")
	
	-- need to find a way to get the spell slot level for internal calculations
	
	damageDice = luajava.newInstance("dnd.combat.DamageDiceGroup", 2, 6, DamageType.COLD)
	return damageDice
end