import groovy.transform.CompileStatic

class Reporter {
	static stepFailed(String message) {
		String result = "Oh! $message"
		println result
		return result
	}
}


class Verifier {
	static String doTheJob() {
		Closure cls = {
			Reporter.stepFailed("my badness!")
		}
		return cls.call()
	}
}
assert Verifier.doTheJob().contains("badness")

@CompileStatic
class Verifier2 {
	static String doTheJob() {
		return Reporter.stepFailed("my goodness!")
	}
}
assert Verifier2.doTheJob().contains("goodness")


Reporter.metaClass.'static'.invokeMethod = { String name, args ->
	if (name == 'stepFailed') {
		String result = "Yah! ${args[0]}"
		println result
		return result
	} else {
		return delegate.metaClass.getStaticMetaMethod(name, args).invoke(delegate, args)
	}
}

// this will pass, because the Verifier class is not annotated with @CompileStatic
assert Verifier.doTheJob().contains('Yah!')

// this will fail, because the Verifier2 class is annotated with @CompileStatic
assert Verifier2.doTheJob().contains('Yah!')
