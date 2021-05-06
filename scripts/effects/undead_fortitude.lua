function define()
	AttackRoll = luajava.bindClass("com.dndsuite.core.events.contests.AttackRoll")
	Damage = luajava.bindClass("com.dndsuite.core.events.Damage")
	DamageType = luajava.bindClass("com.dndsuite.dnd.data.DamageType")
	Entity = luajava.bindClass("com.dndsuite.core.gameobjects.Entity")
	
	self:setName("Undead Fortitude")
end



function processEvent()
	if target == self:getTarget() then
		if event:hasTag(Damage:getEventID()) then
			local damage = event:getDamage()
			if damage >= target:getHealthTotal() then
				if event:hasDamageType(DamageType.RADIANT) then
					print("[LUA]  Undead Fortitude countered by radiant damage")
					return
				elseif event:getParent():hasTag(AttackRoll.CRITICAL_HIT) then
					print("[LUA]  Undead Fortitude countered by critical hit")
					return
				end
				
				event:applyEffect(self)
				local dc = 5 + damage;
				local savingthrow = luajava.newInstance("com.dndsuite.core.events.contests.SavingThrow", Entity.CON, nil, target)
				savingthrow:invokeEvent(target, target)
				
				if savingthrow:getRoll() >= dc then
					print("[LUA]  Undead Fortitude keeps " .. target:toString() .. " standing! (" .. savingthrow:getRoll() .. " >= " .. dc .. ")")
					event:addTag(Damage.WITHSTOOD)
				else
					print("[LUA]  Undead Fortitude fails! (" .. savingthrow:getRoll() .. " < " .. dc .. ")")
				end
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