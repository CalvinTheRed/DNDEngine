function define()
	effect:setName("Cat Swarm")
end



function processEvent()
	if target == effect:getTarget() then
		if event:hasTag("Attack Roll") then -- and event:hasTag("Melee") then
			event:applyEffect(effect)
			event:addTag("Disadvantage")
		elseif event:hasTag("Saving Throw") and event:hasTag("Dex") then
			event:applyEffect(effect)
			event:addTag("Disadvantage")
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