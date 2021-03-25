function define()
	AttackRoll = luajava.bindClass("com.dndsuite.core.events.contests.AttackRoll")
	Entity = luajava.bindClass("com.dndsuite.core.gameobjects.Entity")
	Event = luajava.bindClass("com.dndsuite.core.events.Event")
	
	self:setName("Ice Knife (" .. Entity:getAbility(self:getAttackAbility()) .. ")")
	self:setAttackType(AttackRoll.RANGED)
	self:setRange(60.0, 60.0)
	
	self:addTag(Event.SINGLE_TARGET)
	self:addTag(Event.SPELL)
	self:addTag("Ice Knife")
end



function additionalEffects()
	secondaryEvent = luajava.newInstance("com.dndsuite.core.events.contests.SavingThrow", "scripts/events/contests/savingthrows/ice_knife_secondary.lua", self:getAttackAbility())
	secondaryEvent:invoke(source, target:getPos())
end



function additionalEffectsOnMiss()
	additionalEffects()
end



function damage()
	DamageType = luajava.bindClass("com.dndsuite.dnd.data.DamageType")
	
	damageDice = luajava.newInstance("com.dndsuite.dnd.combat.DamageDiceGroup", 1, 10, DamageType.PIERCING_MAGICAL)
	return damageDice
end