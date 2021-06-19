package com.kazurayam.ks.execution

import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.webui.keyword.internal.WebUIKeywordMain
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

@RunWith(JUnit4.class)
public class WebUIKeywordMainModifierTest {

	@Before
	void setup() {
		WebUIKeywordMainModifier.modify()
	}

	@Ignore
	@Test
	void test_smoke() {
		WebUIKeywordMain.stepFailed("Oh! My Badness", FailureHandling.OPTIONAL, null, true)
	}

	@Test
	void test_verifyElementPresent() {
		String xpath = "//a[@id='btn_make_DISappointment']"
		TestObject tObj = new TestObject(xpath)
		tObj.addProperty("xpath", ConditionType.EQUALS, xpath)
		WebUI.openBrowser("http://demoaut.katalon.com/")
		WebUI.verifyElementPresent(tObj, 1, FailureHandling.OPTIONAL)
		WebUI.closeBrowser()
	}

	@Ignore
	@Test
	void test_runKeyword() {
		WebUIKeywordMain.runKeyword({
			WebUIKeywordMain.stepFailed("Hello", FailureHandling.OPTIONAL, null, true)
		}, FailureHandling.OPTIONAL, true, "My Goodness")
	}
}










