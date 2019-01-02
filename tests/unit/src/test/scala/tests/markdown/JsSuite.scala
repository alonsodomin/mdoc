package tests.markdown

import StringSyntax._

class JsSuite extends BaseMarkdownSuite {

  def suffix(name: String): String =
    s"""|<script type="text/javascript" src="$name.md.js" defer></script>
        |
        |<script type="text/javascript" src="mdoc.js" defer></script>
        |""".stripMargin

  check(
    "basic",
    """
      |```scala mdoc:js
      |println("hello world!")
      |```
    """.stripMargin,
    """|```scala
       |println("hello world!")
       |```
       |
       |<div id="mdoc-js-run0" data-mdoc-js></div>
       |
       |<script type="text/javascript" src="basic.md.js" defer></script>
       |
       |<script type="text/javascript" src="mdoc.js" defer></script>
    """.stripMargin
  )

  checkError(
    "error",
    """
      |```scala mdoc:js
      |val x: Int = ""
      |```
    """.stripMargin,
    """
      |error: error.md:3:14: error: type mismatch;
      | found   : String("")
      | required: Int
      |val x: Int = ""
      |             ^^
    """.stripMargin
  )

  check(
    "multi",
    """
      |```scala mdoc:js
      |println("hello 1!")
      |```
      |
      |```scala mdoc:js
      |println("hello 2!")
      |```
    """.stripMargin,
    """|```scala
       |println("hello 1!")
       |```
       |
       |<div id="mdoc-js-run0" data-mdoc-js></div>
       |
       |```scala
       |println("hello 2!")
       |```
       |
       |<div id="mdoc-js-run1" data-mdoc-js></div>
       |
       |<script type="text/javascript" src="multi.md.js" defer></script>
       |
       |<script type="text/javascript" src="mdoc.js" defer></script>
       |""".stripMargin
  )

  checkError(
    "edit",
    """
      |```scala mdoc:js
      |val x: Int = ""
      |```
      |
      |```scala mdoc:js
      |val y: String = 42
      |```
    """.stripMargin,
    """|error: edit.md:3:14: error: type mismatch;
       | found   : String("")
       | required: Int
       |val x: Int = ""
       |             ^^
       |error: edit.md:7:17: error: type mismatch;
       | found   : Int(42)
       | required: String
       |val y: String = 42
       |                ^^
    """.stripMargin
  )

  checkError(
    "isolated",
    """
      |```scala mdoc:js
      |val x = 1
      |```
      |
      |```scala mdoc:js
      |println(x)
      |```
    """.stripMargin,
    """|error: isolated.md:7:9: error: not found: value x
       |println(x)
       |        ^
    """.stripMargin
  )

  checkCompiles(
    "mountNode",
    """
      |```scala mdoc:js
      |node.innerHTML = "<h3>Hello world!</h3>"
      |```
    """.stripMargin
  )

  check(
    "shared",
    """
      |```scala mdoc:js:shared
      |val x = 1
      |```
      |
      |```scala mdoc:js
      |println(x)
      |```
    """.stripMargin,
    """|```scala
       |val x = 1
       |```
       |
       |```scala
       |println(x)
       |```
       |
       |<div id="mdoc-js-run1" data-mdoc-js></div>
       |
       |<script type="text/javascript" src="shared.md.js" defer></script>
       |
       |<script type="text/javascript" src="mdoc.js" defer></script>
    """.stripMargin
  )

  // It's easy to mess up stripMargin multiline strings when generating code with strings.
  check(
    "stripMargin",
    """
      |```scala mdoc:js
      |val x = '''
      |  |a
      |  | b
      |  |  c
      | '''.stripMargin
      |```
    """.stripMargin.triplequoted,
    s"""|```scala
        |val x = '''
        |  |a
        |  | b
        |  |  c
        | '''.stripMargin
        |```
        |
        |<div id="mdoc-js-run0" data-mdoc-js></div>
        |
        |${suffix("stripMargin")}
        |""".stripMargin.triplequoted
  )

  check(
    "invisible",
    """
      |```scala mdoc:js:invisible
      |println("Hello!")
      |```
    """.stripMargin,
    s"""|
        |<div id="mdoc-js-run0" data-mdoc-js></div>
        |
        |${suffix("invisible")}
        |""".stripMargin
  )

  checkCompiles(
    "deps",
    """
      |```scala mdoc:js
      |println(jsapp.ExampleJS.greeting)
      |```
    """.stripMargin
  )
}