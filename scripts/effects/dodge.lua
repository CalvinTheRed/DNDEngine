function define()
	self:setName("Dodge")
end



function processEvent()
	Entity = luajava.bindClass("com.dndsuite.core.gameobjects.Entity")
	DiceContest = luajava.bindClass("com.dndsuite.core.events.contests.DiceContest")
	AttackRoll = luajava.bindClass("com.dndsuite.core.events.contests.AttackRoll")
	SavingThrow = luajava.bindClass("com.dndsuite.core.events.contests.SavingThrow")
	
	if target == self:getTarget() then
		if event:hasTag(AttackRoll:getEventID()) then
			event:applyEffect(self)
			event:addTag(DiceContest.DISADVANTAGE)
		elseif event:hasTag(SavingThrow:getEventID()) and event:getSaveAbility() == Entity.DEX then
			event:applyEffect(self)
			event:addTag(DiceContest.ADVANTAGE)
		else
			print("[LUA]  No changes made to " .. event:toString())
		end
	else
		print("[LUA]  No changes made to " .. event:toString())
	end
end



function processEventSafe()
	print("[LUA]  Effect " .. self:toString() .. " processing event " .. event:toString() .. "...")
	if not pcall(processEvent) then
		print("[LUA]  Effect " .. self:toString() .. " already applied to event " .. event:toString())
	end
end