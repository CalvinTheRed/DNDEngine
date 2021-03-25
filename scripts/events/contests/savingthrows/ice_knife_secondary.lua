function define()
	Entity = luajava.bindClass("com.dndsuite.core.gameobjects.Entity")
	Event = luajava.bindClass("com.dndsuite.core.events.Event")
	
	self:setName("Ice Knife (" .. Entity:getAbility(self:getDCAbility()) .. ", Secondary)")
	self:setSaveAbility(Entity.DEX)
	self:setRange(0.0, 0.0)
	self:setRadius(5.0)
	
	self:addTag(Event.SPHERE)
	self:addTag(Event.SPELL)
	self:addTag("Ice Knife")
end



function additionalEffects()
end



function additionalEffectsOnPass()
end



function damage()
	DamageType = luajava.bindClass("com.dndsuite.dnd.data.DamageType")
	
	-- need to find a way to get the spell slot level for internal calculations
	
	damageDice = luajava.newInstance("com.dndsuite.dnd.combat.DamageDiceGroup", 2, 6, DamageType.COLD)
	return damageDice
end