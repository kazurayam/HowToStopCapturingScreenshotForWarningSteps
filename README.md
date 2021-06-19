How to stop capturing screenshots when a Katalon Studio keyword fails with FailureHandling.OPTIONAL
=====

# Problem to solve

This project is about a post in the Katalon Forum:

- https://forum.katalon.com/t/how-to-stop-capturing-screenshot-for-warning-steps/18705

The problem discussed was that Katalon Studio' built-in keywords emit page screenshots on failure even when Failure.OPTIONAL is specified. Some people see that their test suite captures many screenshots. Hence there report becomes very heavy, difficult to manage. They argue that keywords should not emit screenshots when FailureHandling.OPTIONAL is specified. They expect FailureHandling.OPTIONAL to give warning messages only. They do not expect screenshots to be generated.

# What I intended to achieve

com.kms.katalon.core.webui.keyword.internal.WebUIKeywordMain has a method StepFailed.

The current source looks like this:

```
    public static stepFailed(String message, FailureHandling flHandling, Throwable t, boolean takeScreenShot)
    throws StepFailedException {
        KeywordMain.stepFailed(message, flHandling, new StepFailedException(message, t), 
            new WebUIScreenCaptor().takeScreenshotAndGetAttributes(takeScreenShot));
    }
```

As this code tells, when a WebUI keyword has got a failure (for example, a HTML element searched was not found in the page), regardless which FailureHandling is selected, a screenshot will be taken.

If Katalon Team changes the code as follows, @Lakshminarayana_D and other people would be happy.

```
    public static stepFailed(String message, FailureHandling flHandling, Throwable t, boolean takeScreenShot) throws StepFailedException {
            boolean requireScreenShot = takeScreenShot
            if (flHandling == FailureHandling.OPTIONAL) {
                    requireScreenShot = false
            }
            KeywordMain.stepFailed(message, flHandling, new StepFailedException(message, t),
                   new WebUIScreenCaptor().takeScreenshotAndGetAttributes(requireScreenShot));
    }
```
This code will not take screenshot when FailureHandling.OPTIONAL is assigned.

`Test Suites/main/TS2` demonstrates what I wanted to achieve.

# Difficulty I encountered

I tried to find out a way to change the method implementation dynamically using Groovy’s MetaProgramming technique. I thought it should be possible in theory. But unfortunately, I could not achieve it.

`Test Suites/main/TS2` failes somehow.

# Cause

I have found out a thing. I would conclude that it is impossible to change Katalon Studio’s built-in keywords by Groovy MetaProgramming technique.

The reason is that most of the built-in keywords are annotated with groovy.transform.CompileStatic option. For example, WebUI.verifyElementPresent() keyword’s source looks like this:

```
    @CompileStatic
    public boolean verifyElementPresent(TestObject to, int timeOut, FailureHandling flowControl) throws StepFailedException {
        ...
```

According to the javadoc of CompileStatic:

>This will let the Groovy compiler use compile time checks in the style of Java then perform static compilation, thus bypassing the Groovy meta object protocol.

So any Groovy MetaProgramming technique looses power on the Class/Method annotated with @CompiileStatic.

# Additonal thought

The following 2 keywords function almost similar:

- `WebUI.verifyElementPresent(testobject, 5, FailureHandling.OPTIONAL)` [source](https://github.com/katalon-studio/katalon-studio-testing-framework/blob/master/Include/scripts/groovy/com/kms/katalon/core/webui/keyword/builtin/VerifyElementPresentKeyword.groovy)
- `WebUI.waitForElementPresent(testobject, 5, FailureHandling.OPTIONAL)` [source](https://github.com/katalon-studio/katalon-studio-testing-framework/blob/master/Include/scripts/groovy/com/kms/katalon/core/webui/keyword/builtin/WaitForElementPresentKeyword.groovy)

but there is a difference, which might be significant for you @prajakta:

WebUI.verifyElementPresent() emits screenshot images when the testobject is not found in the web page within the timeout period, but
WebUI.waitForElementPresent() does not emits any screenshots.
If your test cases are using verifyElementPresent keyword, I would recommend you to change your test: use waitForElementPreset rather than verifyElementPresent. Then you will have less amount of screenshots.

It depends on each indivisual keyword’s implementation if it emits screenshot or not. You should read the source of keywords. If a keyword is calling WebUIKeywordMain.stepFailed(xxx,xxx,xxx,xxx) method, then it would emit screenshots on failure. In general, WebUI.verifyXXX keywords emits screenshot, WebUI.waitForXXXX keywords don’t.
