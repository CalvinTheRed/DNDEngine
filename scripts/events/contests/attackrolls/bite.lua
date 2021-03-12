function define()
	ar = luajava.bindClass("core.events.contests.AttackRoll")
	Entity = luajava.bindClass("core.gameobjects.Entity")
	Event = luajava.bindClass("core.events.Event")
	
	event:setName("Bite")
	event:setAttackType(ar.MELEE)
	event:setRange(5.0, 5.0)
	event:addTag("Bite")
	event:addTag(Event.SINGLE_TARGET)
end



function additionalEffects()
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
	
	damageDice = luajava.newInstance("dnd.combat.DamageDiceGroup", numDice, 10, dt.PIERCING)
	damageDice:addBonus(source:getAbilityModifier(event:getAttackAbility()))
	return damageDice
end