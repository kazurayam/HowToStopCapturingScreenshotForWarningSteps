ありがとうございましした　上原潤二さん
=========

- author: kazurayam
- date: 20 JUNE 2021

わたしは`com.kazurayam.ks.execution.WebUIKeywordMainModifier`を書いた。

```
public class WebUIKeywordMainModifier {

	public static void modify() {
		println("[WebUIKeywordMainModifier] modify() is called")
		WebUIKeywordMain.metaClass.'static'.invokeMethod = { String name, args ->
            ...
```

というコードを書いた。こうすればKatalon Studio組み込みキーワード`WebUI.verifyElementPresent()`の動作を実行時に変更できるだろうとおもった。`Test Suites/TS2`でやってみたが、うまくいかない。`verifyElementPresent`が`invokeMethod`で設定されたClosureを呼び出すことをわたしは期待した。しかしそうならない。`com.kms.katalon.core.webui.keyword.internal.WebUIKeywordMain`クラスのオリジナルの`stepFailed`メソッドを`verifyElementPresent`が呼び出してしまう。なぜこうなるのか、わからなかった。3日ほど悩んだ。


上原潤二さんが書いた記事 https://magazine.rubyist.net/articles/0037/0037-GuestTalk.html を読んで助けられた。つぎの一文が示唆的だった。

>静的コンパイル・静的型チェックとして正式に再実装されようとしてます。「それはひょっとしたら道を踏み外してるんじゃないか」というハラハラ感もあり目を離せません。

これは `@CompileStatic` のことをいっているとわかった。要注意なのかあ。そこでGroovyDocを読んだ。

- https://docs.groovy-lang.org/latest/html/gapi/groovy/transform/CompileStatic.html

>This will let the Groovy compiler use compile time checks in the style of Java then perform static compilation, thus bypassing the Groovy meta object protocol.

`@CompileStatic`アノテーションが施されたメソッドはそれがGroovy言語で書かれていても、Java言語で書かれたメソッドと同じように動く、つまりGreoovyのMeta Object Protocolがバイパスされ、無効になる。

わたしは`@CompileStatic`がこういうものであることを知らなかった。

ああ、そういうことか。再現テストを作った。

- [CompileStaticBypassesMetaObjectProtocol](./Scripts/research/CompileStaticBypassesMetaObjectProtocol/Script1624084842942.groovy)

なあるほど。これで`verifyElementPresent`が`invokeMethod`で動的に設定されたClosureを実行してくれない理由がわかった。

あきらめがついた。よかった。

