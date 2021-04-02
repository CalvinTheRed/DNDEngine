function define()
	DamageType = luajava.bindClass("com.dndsuite.dnd.data.DamageType")
	Entity = luajava.bindClass("com.dndsuite.core.gameobjects.Entity")
	Event = luajava.bindClass("com.dndsuite.core.events.Event")
	VirtualBoard = luajava.bindClass("com.dndsuite.dnd.VirtualBoard")
	
	self:setName("Ice Knife (" .. Entity:getAbilityString(self:getEventAbility()) .. ")")
	self:setRange(60.0, 60.0)
	self:setRadius(0.0);
	
	self:addTag(Event.SPELL)
	self:addTag("Ice Knife")
end



function damage()
	damageDice = luajava.newInstance("com.dndsuite.dnd.combat.DamageDiceGroup", 1, 10, DamageType.PIERCING_MAGICAL)
	return damageDice
end



function invokeEvent()
	local attackroll = luajava.newInstance("com.dndsuite.core.events.contests.AttackRoll", self:getEventAbility(), self, target)
	attackroll:invokeEvent(source, target)
	
	local acc = luajava.newInstance("com.dndsuite.core.events.ArmorClassCalculation", target)
	acc:invokeEvent(source, target)
	
	if attackroll:getRoll() >= acc:getAC() then
		print("[Lua]  Attack roll hit! (" .. attackroll:getRoll() .. " >= " .. acc:getAC() .. ")")
		local damagecalc = luajava.newInstance("com.dndsuite.core.events.DamageCalculation", self)
		damagecalc:addDamageDiceGroup(damage())
		damagecalc:roll(attackroll)
		damagecalc:invokeEvent(source, target)
	else
		print("[Lua]  Attack roll miss! (" .. attackroll:getRoll() .. " < " .. acc:getAC() .. ")")
	end
	
	secondaryInvoke()
end



function secondaryInvoke()
	local nextevent = luajava.newInstance("com.dndsuite.core.events.Event", "scripts/events/contests/ice_knife_secondary.lua", self:getEventAbility())
	nextevent:invoke(source, target:getPos())
end



function targets()
	return VirtualBoard:nearestObject(targetPos, {Entity:getGameObjectID()})
end