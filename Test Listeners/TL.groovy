import com.kazurayam.ks.execution.WebUIKeywordMainModifier
import com.kms.katalon.core.annotation.BeforeTestSuite
import com.kms.katalon.core.annotation.BeforeTestCase 
import com.kms.katalon.core.context.TestSuiteContext
import com.kms.katalon.core.context.TestCaseContext

class TL {
	@BeforeTestSuite
	def beforeTestSuite(TestSuiteContext testSuiteContext) {
		if (testSuiteContext.getTestSuiteId().endsWith("TS2")) {
			println("[TL] starting TS2")
			WebUIKeywordMainModifier.modify()
		}
	}
	/*
	@BeforeTestCase
	def beforeTestCase(TestCaseContext testCaseContext) {
		StopTakingScreenshotWhenOptional.modify()
	}
	*/
}