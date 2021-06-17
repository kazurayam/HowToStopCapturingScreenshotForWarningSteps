package com.kazurayam.ks.execution

import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.internal.KeywordMain
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.webui.helper.screenshot.WebUIScreenCaptor
import com.kms.katalon.core.webui.keyword.internal.WebUIKeywordMain

public class StopTakingScreenshotWhenOptional {

	public static void manage() {
		println("[StopTakingScreenshotWhenOptional] manage() is called")
		WebUIKeywordMain.metaClass.'static'.invokeMethod = { String name, args ->
			if (name == "stepFailed") {
				println("[StopTakingScreenshotWhenOptional.manage] WebUIKeywordMain.stepFailed method is executed in a special way")
				assert args.size() == 4
				// invoke the specialized 'stepFailed' method
				String message = args[0]
				FailureHandling flHandling = args[1]
				Throwable t = args[2]
				boolean takeScreenShot = args[3]
				if (flHandling == FailureHandling.OPTIONAL) {
					//we do not take screenshot when we apply FailureHandling.OPTIONAL
					takeScreenShot = false
				}
				return KeywordMain.stepFailed(message, flHandling, new StepFailedException(message, t),
						new WebUIScreenCaptor().takeScreenshotAndGetAttributes(takeScreenShot));
			} else {
				// do as usual
				println("[StopTakingScreenshotWhenOptional.manage] WebUIKeywordMain.${name} method is executed as usual")
				return delegate.metaClass.getStaticMetaMethod(name, args).invoke(delegate, args)
			}
		}
	}
}
