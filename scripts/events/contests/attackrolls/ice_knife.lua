function define()
DamageType = luajava.bindClass("com.dndsuite.dnd.data.DamageType")
	Entity = luajava.bindClass("com.dndsuite.core.gameobjects.Entity")
	Event = luajava.bindClass("com.dndsuite.core.events.Event")
	VirtualBoard = luajava.bindClass("com.dndsuite.dnd.VirtualBoard")
	
	self:setName("Ice Knife (" .. Entity:getAbility(self:getEventAbility()) .. ")")
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
	attackroll = luajava.newInstance("com.dndsuite.core.events.contests.AttackRoll", self:getEventAbility(), self, target)
	attackroll:invoke(source, target:getPos())
	acc = luajava.newInstance("com.dndsuite.core.events.ArmorClassCalculation", target)
	
	print(attackroll:getRoll() .. ", " .. acc:getAC())
	
	if attackroll:getRoll() >= acc:getAC() then
		damagecalc = luajava.newInstance("com.dndsuite.core.events.DamageCalculation", self)
		nearest = VirtualBoard:nearestObject(targetPos, {})
		damagecalc:setTargets(VirtualBoard:nearestObject(nearest:getPos(), {}))
		damagecalc:addDamageDiceGroup(damage())
		damagecalc:invoke(source, nearest:getPos())
	end
	
	secondaryInvoke()
end



function hit()
end



function miss()
end



function secondaryInvoke()
end



function targets()
	return VirtualBoard:nearestObject(targetPos, {Entity:getGameObjectID()})
end