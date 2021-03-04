function define()
	effect:setName("Dodge")
end



function processEvent()
	if target == effect:getTarget() then
		if event:hasTag("Attack Roll") then
			event:applyEffect(effect)
			event:addTag("Disadvantage")
		elseif event:hasTag("Saving Throw") and event:hasTag("Dex") then
			event:applyEffect(effect)
			event:addTag("Advantage")
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