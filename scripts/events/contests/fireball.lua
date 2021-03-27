function define()
	DamageCalculation = luajava.bindClass("com.dndsuite.core.events.DamageCalculation")
	DamageType = luajava.bindClass("com.dndsuite.dnd.data.DamageType")
	Entity = luajava.bindClass("com.dndsuite.core.gameobjects.Entity")
	Event = luajava.bindClass("com.dndsuite.core.events.Event")
	VirtualBoard = luajava.bindClass("com.dndsuite.dnd.VirtualBoard")
	
	self:setName("Fireball (" .. Entity:getAbility(self:getEventAbility()) .. ")")
	self:setRange(150.0, 150.0)
	self:setRadius(20.0)
	
	self:addTag(Event.SPELL)
	self:addTag("Fireball")
end



function damage()
	if damagedice == nil then
		damagedice = luajava.newInstance("com.dndsuite.dnd.combat.DamageDiceGroup", 8, 6, DamageType.FIRE)
		damagedice:roll()
	end
	return damagedice:clone()
end



function invokeEvent()
	local savingthrow = luajava.newInstance("com.dndsuite.core.events.contests.SavingThrow", self:getEventAbility(), self, target)
	savingthrow:invokeEvent(source, target)
	
	local dcc = luajava.newInstance("com.dndsuite.core.events.DiceCheckCalculation", self:getEventAbility(), target)
	dcc:invokeEvent(source, target)
	
	local damagecalc = luajava.newInstance("com.dndsuite.core.events.DamageCalculation", self)
	damagecalc:addDamageDiceGroup(damage())
	
	if savingthrow:getRoll() < dcc:getDC() then
		print("[Lua]  Saving throw fail! (" .. savingthrow:getRoll() .. " < " .. dcc:getDC() .. ")")
	else
		print("[Lua]  Saving throw pass! (" .. savingthrow:getRoll() .. " >= " .. dcc:getDC() .. ")")
		damagecalc:addTag(DamageCalculation.HALVED)
	end
	
	damagecalc:invokeEvent(source, target)
end



function targets()
	return VirtualBoard:objectsInSphere(targetPos, self:getRadius(), {Entity:getGameObjectID()})
end