function define()
	self:setName("Undead Fortitude")
end



function processEvent()
	AttackRoll = luajava.bindClass("com.dndsuite.core.events.contests.AttackRoll")
	DamageDealt = luajava.bindClass("com.dndsuite.core.events.DamageDealt")
	DamageType = luajava.bindClass("com.dndsuite.dnd.data.DamageType")
	Entity = luajava.bindClass("com.dndsuite.core.gameobjects.Entity")
	
	if target == self:getTarget() then
		if event:hasTag(DamageDealt:getEventID()) then
			if event:getDamage() >= target:getHealth() then
				if event:hasDamageType(DamageType.RADIANT) then
					print("[LUA]  Undead Fortitude countered by radiant damage")
					return
				elseif event:getParent():hasTag(AttackRoll.CRITICAL_HIT) then
					print("[LUA]  Undead Fortitude countered by critical hit")
					return
				end
				
				-- do stuff
			end
		end
	end
end



function processEventSafe()
	processEvent()
	--if not pcall(processEvent) then
		--print("[LUA]  Effect " .. self:toString() .. " already applied to event " .. event:toString())
	--end
end