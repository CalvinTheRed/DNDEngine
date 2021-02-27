function invokeEvent()

-- see more: http://www.luaj.org/luaj/3.0/README.html#luajava

print("[LUA] Applying Dodge effect to " .. source:toString())

vb = luajava.bindClass("engine.VirtualBoard")
target = vb:entityAt(targetPos)

effect = luajava.newInstance("dnd.effects.Dodge", source, target)
source:addEffect(effect)

print("[LUA] Done")

end



function getVal()

return 10,20

end