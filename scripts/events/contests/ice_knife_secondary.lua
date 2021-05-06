function define()
	DamageType = luajava.bindClass("com.dndsuite.dnd.data.DamageType")
	Entity = luajava.bindClass("com.dndsuite.core.gameobjects.Entity")
	Event = luajava.bindClass("com.dndsuite.core.events.Event")
	VirtualBoard = luajava.bindClass("com.dndsuite.dnd.VirtualBoard")
	
	self:setName("Ice Knife (" .. Entity:getAbilityString(self:getEventAbility()) .. ", Secondary)")
	self:setRange(0.0, 0.0)
	self:setRadius(5.0)
	
	self:addTag(Event.SPELL)
	self:addTag("Ice Knife")
end



function damage()
	if damagedice == nil then
		damagedice = luajava.newInstance("com.dndsuite.dnd.combat.DamageDiceGroup", 2, 6, DamageType.COLD)
		damagedice:roll()
	end
	return damagedice:clone()
end



function invokeEvent()
	local savingthrow = luajava.newInstance("com.dndsuite.core.events.contests.SavingThrow", self:getEventAbility(), self, target)
	savingthrow:invokeEvent(source, target)
	
	local dcc = luajava.newInstance("com.dndsuite.core.events.DiceCheckCalculation", self:getEventAbility(), target)
	dcc:invokeEvent(source, target)
	
	if savingthrow:getRoll() < dcc:getDC() then
		print("[Lua]  Saving throw fail! (" .. savingthrow:getRoll() .. " < " .. dcc:getDC() .. ")")
		local damagecalc = luajava.newInstance("com.dndsuite.core.events.DamageCalculation", self)
		damagecalc:addDamageDiceGroup(damage())
		damagecalc:invokeEvent(source, target)
	else
		print("[Lua]  Saving throw pass! (" .. savingthrow:getRoll() .. " >= " .. dcc:getDC() .. ")")
	end
end



function targets()
	return VirtualBoard:objectsInSphere(targetPos, self:getRadius(), {Entity:getGameObjectID()})
end