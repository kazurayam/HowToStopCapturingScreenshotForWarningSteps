class Stuff {
	static invokeMe() { "foo" }
 }
 
 Stuff.metaClass.'static'.invokeMethod = { String name, args ->
	def metaMethod = Stuff.metaClass.getStaticMetaMethod(name, args)
	def result
	if (metaMethod) {
		result = metaMethod.invoke(delegate,args)
	} else {
	   result = "bar"
	}
	result
 }
 
 assert "foo" == Stuff.invokeMe()
 assert "bar" == Stuff.doStuff()