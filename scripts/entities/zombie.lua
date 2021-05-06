function define()
	DamageType = luajava.bindClass("com.dndsuite.dnd.data.DamageType")
	Entity = luajava.bindClass("com.dndsuite.core.gameobjects.Entity")
	
	-- Set name
	self:setName("Zombie")
	
	-- Apply all relevant tags
	self:addTag(Entity.UNDEAD)
	
	-- Set challenge data
	self:setLevel(3)
	self:setExperience(50)
	
	-- Set health data
	self:setHealthBase(22)
	self:setHealthMax(22)
	self:setHealth(22)
	self:setHealthTmp(0)
	
	-- Populate inventory
	
	-- Add base tasks
	task = luajava.newInstance("com.dndsuite.core.tasks.Task", "scripts/tasks/zombie_slam.lua")
	self:addBaseTask(task)
	task = luajava.newInstance("com.dndsuite.core.tasks.Attack", self)
	self:addBaseTask(task)
	
	-- Add active effects
	effect = luajava.newInstance("com.dndsuite.core.effects.Effect", "scripts/effects/undead_fortitude.lua", self, self)
	self:addEffect(effect)
	effect = luajava.newInstance("com.dndsuite.core.effects.DamageImmunity", self, self, DamageType.POISON)
	self:addEffect(effect)
	effect = luajava.newInstance("com.dndsuite.core.effects.SaveProficiency", self, self, Entity.WIS)
	self:addEffect(effect)
	
	-- Add item proficiencies
	
	-- Set held and worn equipment
	
	-- Set ability scores
	self:setBaseAbilityScore(Entity.STR, 13)
	self:setBaseAbilityScore(Entity.DEX, 6)
	self:setBaseAbilityScore(Entity.CON, 16)
	self:setBaseAbilityScore(Entity.INT, 3)
	self:setBaseAbilityScore(Entity.WIS, 6)
	self:setBaseAbilityScore(Entity.CHA, 5)
end