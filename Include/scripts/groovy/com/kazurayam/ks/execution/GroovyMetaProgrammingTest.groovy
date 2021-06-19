package com.kazurayam.ks.execution
/**
 * Rubyist Magazine, 他言語からの訪問【第2回】Groovy(後編)
 * https://magazine.rubyist.net/articles/0037/0037-GuestTalk.html#methodmissing-%E3%81%A8-invokemethod
 * 上原潤二
 *
 * キーワード: 実行時メタプログラミング,カテゴリ,ExpandoMetaClass,methodMissing,invokeMethod,MOP,delegate
 */

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

class HelloCategory {
	static String hello(String self) {
		return "hello $self"
	}
}

@RunWith(JUnit4.class)
public class GroovyMetaProgrammingTest {

	@Before
	void setup() {
	}

	@Test
	void test_category() {
		assert HelloCategory.hello("me") == "hello me"
		use (HelloCategory) {
			assert "world".hello() == "hello world"
		}
		try {
			"world".hello()
		} catch (Exception e) {
			assert e.getMessage().startsWith("No signature of method: java.lang.String.hello()")
		}
	}

	@Test
	void test_ExpandoMetaClass() {
		String.metaClass.hello = {
			return "hello $delegate"
		}
		assert "ABC".hello() == "hello ABC"
	}

	@Test
	void test_ExpandoMetaClass_instance() {
		String s = "ABC"
		s.metaClass.hello = {
			return "hello $delegate"
		}
		assert s.hello() == "hello ABC"
		try {
			"DEF".hello()
		} catch (Exception e) {
			assert e.getMessage().startsWith("No signature of method: java.lang.String.hello()")
		}
	}

	@Test
	void test_methodMissing() {
		String.metaClass.methodMissing = { String methodName, args ->
			return "method ${delegate}.${methodName}(${args}) was called"
		}
		assert "ABC".harehoe() == "method ABC.harehoe([]) was called"
	}

	@Test
	/**
	 * invokeMethodを定義するとメソッドが存在していようといまいとinvokeMethodのほうが
	 * 呼び出され、本来のメソッドは呼び出されなくなります。Groovyのメタプログラミングでは
	 * invokeMethodのトラップは多用されます。
	 */
	void test_invokeMethod() {
		BigDecimal.metaClass.invokeMethod = { String methodName, args ->
			"method ${delegate}.${methodName}(${args}) was called"
		}
		assert (3.3 + 4.5) == "method 3.3.plus([4.5]) was called"
	}

	/**
	 * ちなみに、メタクラス（含むExpandoMetaClassやカテゴリ）によるメソッド追加は
	 * Groovyコードの動的メソッドディスパッチの仕組みの中で実現されており、
	 * Groovyで書かれたコードからはクラスにメソッドが追加されたように見えるのですが、
	 * Javaで定義されたコード、たとえば下位層にあるJavaフレームワークやJava標準
	 * クラスライブラリからは見えず、それらを誤動作させるようにGroovy側から修正することは
	 * できません。また、同様の理由によりJavaのメソッドを削除したり上書きすることもできません。
	 * つまりGroovyは「Javaの上に動的言語のレイヤを被せるもの」であり、「下位層のJavaの
	 * 動作に侵襲して動的にする」ことはできません。
	 */

}


