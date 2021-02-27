function invokeEvent()

-- see more: http://www.luaj.org/luaj/3.0/README.html#luajava

print("[LUA] Applying Dodge effect to " .. source:toString())

-- find way to utilize VirtualBoard search functions

effect = luajava.newInstance("dnd.effects.Dodge", source, source)
source:addEffect(effect)

print("[LUA] Done")

end