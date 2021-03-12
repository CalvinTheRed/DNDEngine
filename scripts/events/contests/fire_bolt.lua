function define()
	ar = luajava.bindClass("core.events.contests.AttackRoll")
	dc = luajava.bindClass("core.events.contests.DiceContest")
	Entity = luajava.bindClass("core.gameobjects.Entity")
	Event = luajava.bindClass("core.events.Event")
	
	event:setName("Fire Bolt (" .. Entity:getAbility(event:getAttackAbility()) .. ")")
	event:setAttackType(ar.RANGED)
	event:setRange(120.0, 120.0)
	
	event:addTag(dc.SPELL)
	event:addTag(Event.SINGLE_TARGET)
	
	event:addTag("Fire Bolt")
	event:addTag("Cantrip")
end



function additionalEffects()
end



function additionalEffectsOnMiss()
end



function damage()
	ar = luajava.bindClass("core.events.contests.AttackRoll")
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
	
	damageDice = luajava.newInstance("dnd.combat.DamageDiceGroup", numDice, 10, dt.FIRE)
	return damageDice
end