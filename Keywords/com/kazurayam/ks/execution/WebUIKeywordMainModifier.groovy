package com.kazurayam.ks.execution

import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.internal.KeywordMain
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.webui.helper.screenshot.WebUIScreenCaptor
import com.kms.katalon.core.webui.keyword.internal.WebUIKeywordMain

public class WebUIKeywordMainModifier {

	public static void modify() {
		println("[WebUIKeywordMainModifier] modify() is called")
		WebUIKeywordMain.metaClass.'static'.invokeMethod = { String name, args ->
			if (name == "stepFailed") {
				println("[WebUIKeywordMainModifier] WebUIKeywordMain.stepFailed method is modified and invoked")
				assert args.size() == 4
				String message = args[0]
				FailureHandling flHandling = args[1]
				Throwable t = args[2]
				boolean takeScreenShot = args[3]
				// I do not want screenshot when FailureHandling.OPTIONAL is given
				if (flHandling == FailureHandling.OPTIONAL) {
					takeScreenShot = false
				}
				KeywordMain.stepFailed(message, flHandling, new StepFailedException(message, t),
						new WebUIScreenCaptor().takeScreenshotAndGetAttributes(takeScreenShot));
			} else {
				// do as usual
				println("[WebUIKeywordMainModifier] WebUIKeywordMain.${name} method is executed as usual")
				delegate.metaClass.getStaticMetaMethod(name, args).invoke(delegate, args)
			}
		}
	}

}
