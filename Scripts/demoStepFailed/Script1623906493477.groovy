import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.webui.keyword.internal.WebUIKeywordMain

WebUIKeywordMain.stepFailed("Hello, world!", FailureHandling.OPTIONAL,
	new IllegalArgumentException("foo"), true)