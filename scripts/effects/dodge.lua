function define()
	effect:setName("Dodge")
end



function processEvent()
	Entity = luajava.bindClass("core.gameobjects.Entity")
	DiceContest = luajava.bindClass("core.events.contests.DiceContest")
	AttackRoll = luajava.bindClass("core.events.contests.AttackRoll")
	SavingThrow = luajava.bindClass("core.events.contests.SavingThrow")
	
	if target == effect:getTarget() then
		if event:hasTag(AttackRoll:getEventID()) then
			event:applyEffect(effect)
			event:addTag(DiceContest.DISADVANTAGE)
		elseif event:hasTag(SavingThrow:getEventID()) and event:getSaveAbility() == Entity.DEX then
			event:applyEffect(effect)
			event:addTag(DiceContest.ADVANTAGE)
		else
			print("[LUA]  No changes made to " .. event:toString())
		end
	else
		print("[LUA]  No changes made to " .. event:toString())
	end
end



function processEventSafe()
	print("[LUA]  Effect " .. effect:toString() .. " processing event " .. event:toString() .. "...")
	if not pcall(processEvent) then
		print("[LUA]  Effect " .. effect:toString() .. " already applied to event " .. event:toString())
	end
end