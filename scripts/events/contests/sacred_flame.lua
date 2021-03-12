function define()
	dc = luajava.bindClass("core.events.contests.DiceContest")
	Entity = luajava.bindClass("core.gameobjects.Entity")
	Event = luajava.bindClass("core.events.Event")
	
	event:setName("Sacred Flame (" .. Entity:getAbility(event:getDCAbility()) .. ")")
	event:setSaveAbility(Entity.DEX)
	
	event:addTag(dc.SPELL)
	event:addTag(Event.SINGLE_TARGET)
	
	event:addTag("Sacred Flame")
	event:addTag("Cantrip")
	event:addTag("Dex")
	
end



function additionalEffects()
end



function additionalEffectsOnPass()
end



function damage()
	dt = luajava.bindClass("dnd.data.DamageType")
	
	level = source:getLevel()
	numDice = 1
	if level >= 17 then
		numDice = 4
	elseif level >= 11 then
		numDice = 3
	elseif level >= 5 then
		numDice = 2
	end
	
	damageDice = luajava.newInstance("dnd.combat.DamageDiceGroup", numDice, 8, dt.RADIANT)
	return damageDice
end