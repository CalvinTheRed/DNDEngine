function define()
	DamageCalculation = luajava.bindClass("com.dndsuite.core.events.DamageCalculation")
	DamageType = luajava.bindClass("com.dndsuite.dnd.data.DamageType")
	
	self:setName("Poison Damage Immunity")
end



function processEvent()
	if target == self:getTarget() then
		if event:hasTag(DamageCalculation:getEventID()) then
			local damageDice = event:getDamageDice()
			local i = 0
			while i < damageDice:size() do
				if damageDice:get(i):getDamageType() == DamageType.POISON then
					event:applyEffect(self)
					damageDice:get(i):grantImmunity()
					return
				end
				i = i + 1
			end
		end
	end
end



function processEventSafe()
	processEvent()
	if not pcall(processEvent) then
		print("[LUA]  Effect " .. self:toString() .. " already applied to event " .. event:toString())
	end
end