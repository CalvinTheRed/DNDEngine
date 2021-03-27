function define()
	-- Initialize GameObject fields
	self:prepGameObject()
	
	-- Set name
	self:setName("Zombie")
	
	-- Apply all relevant tags
	Entity = luajava.bindClass("com.dndsuite.core.gameobjects.Entity")
	self:addTag(Entity.UNDEAD)
	
	-- Set challenge data
	self:setLevel(3)
	self:setExperience(50)
	
	-- Set health data
	self:setHealth(22)
	self:setHealthBase(22)
	self:setHealthMax(22)
	self:setHealthTmp(0)
	
	-- Populate inventory
	
	-- Initialize Entity fields
	self:prepEntity()
	
	-- Add base tasks
	task = luajava.newInstance("com.dndsuite.core.tasks.Task", "scripts/tasks/zombie_slam.lua")
	self:addBaseTask(task)
	
	-- Add active effects
	effect = luajava.newInstance("com.dndsuite.core.effects.Effect", "scripts/effects/undead_fortitude.lua", self, self)
	self:addEffect(effect)
	effect = luajava.newInstance("com.dndsuite.core.effects.Effect", "scripts/effects/poison_damage_immunity.lua", self, self)
	self:addEffect(effect)
	
	-- Add item proficiencies
	
	-- Set held and worn equipment
	
	-- Set ability score data
	self:setBaseAbilityScores({13,6,16,3,6,5})
	self:setAbilityScores({13,6,16,3,6,5})
end