function define()
	dc = luajava.bindClass("core.events.contests.DiceContest")
	Entity = luajava.bindClass("core.gameobjects.Entity")
	Event = luajava.bindClass("core.events.Event")
	
	event:setName("Ice Knife (" .. Entity:getAbility(event:getDCAbility()) .. ", Secondary)")
	event:setSaveAbility(Entity.DEX)
	
	event:addTag(dc.SPELL)
	event:addTag(Event.SPHERE)
	
	event:addTag("Ice Knife")
	event:addTag("Dex")
end



function additionalEffects()
end



function additionalEffectsOnPass()
end



function damage()
	dt = luajava.bindClass("dnd.data.DamageType")
	
	damageDice = luajava.newInstance("dnd.combat.DamageDiceGroup", 2, 6, dt.COLD)
	return damageDice
end