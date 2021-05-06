function define()
	DamageType = luajava.bindClass("com.dndsuite.dnd.data.DamageType")
	Entity = luajava.bindClass("com.dndsuite.core.gameobjects.Entity")
	Event = luajava.bindClass("com.dndsuite.core.events.Event")
	VirtualBoard = luajava.bindClass("com.dndsuite.dnd.VirtualBoard")
	
	self:setName("Slam")
	self:setRange(5.0, 5.0)
	self:setRadius(0.0)
	
	self:addTag("Slam")
end



function damage()
	damageDice = luajava.newInstance("com.dndsuite.dnd.combat.DamageDiceGroup", 1, 6, DamageType.BLUDGEONING)
	return damageDice
end



function invokeEvent()
	local attackroll = luajava.newInstance("com.dndsuite.core.events.contests.AttackRoll", self:getEventAbility(), self, target)
	attackroll:invokeEvent(source, target)
	
	local acc = luajava.newInstance("com.dndsuite.core.events.ArmorClassCalculation", target)
	acc:invokeEvent(source, target)
	
	if attackroll:getRoll() >= acc:getAC() then
		print("[Lua]  Attack roll hit! (" .. attackroll:getRoll() .. " >= " .. acc:getAC() .. ") bonus " .. attackroll:getBonus())
		local damagecalc = luajava.newInstance("com.dndsuite.core.events.DamageCalculation", self)
		local damage = damage()
		damage:addBonus(source:getAbilityModifier(self:getEventAbility()))
		damagecalc:addDamageDiceGroup(damage)
		damagecalc:roll(attackroll)
		damagecalc:invokeEvent(source, target)
	else
		print("[Lua]  Attack roll miss! (" .. attackroll:getRoll() .. " < " .. acc:getAC() .. ") bonus " .. attackroll:getBonus())
	end
end



function targets()
	return VirtualBoard:nearestObject(targetPos, {Entity:getGameObjectID()})
end