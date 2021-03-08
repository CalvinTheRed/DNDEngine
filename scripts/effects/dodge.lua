function define()
	effect:setName("Dodge")
end



function processEvent()
	ar = luajava.bindClass("core.events.contests.AttackRoll")
	dc = luajava.bindClass("core.events.contests.DiceContest")
	
	if target == effect:getTarget() then
		if event:hasTag(ar.EVENT_TAG_ID) then
			event:applyEffect(effect)
			event:addTag(dc.DISADVANTAGE)
		elseif event:hasTag("Saving Throw") and event:hasTag("Dex") then
			event:applyEffect(effect)
			event:addTag(dc.ADVANTAGE)
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