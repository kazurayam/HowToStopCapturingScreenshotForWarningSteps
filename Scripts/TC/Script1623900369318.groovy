import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

WebUI.openBrowser('')
WebUI.navigateToUrl('http://demoaut.katalon.com/')

TestObject tObj = createTestObject("//a[@id='btn-make-DISappointment']")

WebUI.verifyElementPresent(tObj, 3, FailureHandling.OPTIONAL)

WebUI.closeBrowser()

def createTestObject(String xpath) {
	TestObject tObj = new TestObject(xpath)
	tObj.addProperty("xpath", ConditionType.EQUALS, xpath)
	return tObj
}