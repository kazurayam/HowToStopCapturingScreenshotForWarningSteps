import com.kazurayam.ks.execution.StopTakingScreenshotWhenOptional
import com.kms.katalon.core.annotation.BeforeTestSuite
import com.kms.katalon.core.context.TestSuiteContext

class TL {
	@BeforeTestSuite
	def beforeTestSuite(TestSuiteContext testSuiteContext) {
		if (testSuiteContext.getTestSuiteId().endsWith("TS2")) {
			println("[TL] starting TS2")
			StopTakingScreenshotWhenOptional.manage()
		}
	}
}