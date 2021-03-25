function define()
	Entity = luajava.bindClass("com.dndsuite.core.gameobjects.Entity")
	Event = luajava.bindClass("com.dndsuite.core.events.Event")
	
	self:setName("Sacred Flame (" .. Entity:getAbility(self:getDCAbility()) .. ")")
	self:setSaveAbility(Entity.DEX)
	self:setRange(0.0, 0.0)
	
	self:addTag(Event.SINGLE_TARGET)
	self:addTag(Event.SPELL)
	self:addTag(Event.CANTRIP)
	self:addTag("Sacred Flame")
end



function additionalEffects()
end



function additionalEffectsOnPass()
end



function damage()
	DamageType = luajava.bindClass("com.dndsuite.dnd.data.DamageType")
	
	level = source:getLevel()
	numDice = 1
	if level >= 17 then
		numDice = 4
	elseif level >= 11 then
		numDice = 3
	elseif level >= 5 then
		numDice = 2
	end
	
	damageDice = luajava.newInstance("com.dndsuite.dnd.combat.DamageDiceGroup", numDice, 8, DamageType.RADIANT)
	return damageDice
end