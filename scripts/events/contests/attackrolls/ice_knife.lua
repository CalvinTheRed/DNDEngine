function define()
	ar = luajava.bindClass("core.events.contests.AttackRoll")
	dc = luajava.bindClass("core.events.contests.DiceContest")
	Entity = luajava.bindClass("core.gameobjects.Entity")
	Event = luajava.bindClass("core.events.Event")
	
	event:setName("Ice Knife (" .. Entity:getAbility(event:getAttackAbility()) .. ")")
	event:setAttackType(ar.RANGED)
	event:setRange(60.0, 60.0)
	
	event:addTag(dc.SPELL)
	event:addTag(Event.SINGLE_TARGET)
	
	event:addTag("Ice Knife")
end



function additionalEffects()
	secondaryEvent = luajava.newInstance("core.events.contests.SavingThrow", "scripts/events/contests/savingthrows/ice_knife_secondary.lua", event:getAttackAbility())
	secondaryEvent:invoke(source, target:getPos())
end



function additionalEffectsOnMiss()
	additionalEffects()
end



function damage()
	ar = luajava.bindClass("core.events.contests.AttackRoll")
	dt = luajava.bindClass("dnd.data.DamageType")
	
	damageDice = luajava.newInstance("dnd.combat.DamageDiceGroup", 1, 10, dt.PIERCING_MAGICAL)
	return damageDice
end