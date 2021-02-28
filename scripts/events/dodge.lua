
-- see more: http://www.luaj.org/luaj/3.0/README.html#luajava

function invokeEvent()

vb = luajava.bindClass("engine.VirtualBoard")
target = vb:entityAt(targetPos)
effect = luajava.newInstance("dnd.effects.Dodge", source, target)
source:addEffect(effect)

end