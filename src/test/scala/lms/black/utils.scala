package scala.lms.black

import java.io._
import org.scalatest.FunSuite
import utils._

trait TestSuite extends FunSuite {
  val overwriteCheckFiles = false // should be false; temporary set to true only to simplify development

  val prefix = "src/out/"
  val under: String

  def captureOut(thunk: => Unit) = {
    val output = new ByteArrayOutputStream()
    scala.Console.withOut(new PrintStream(output)) {
      thunk
    }
    output.toString()
  }
  def checkOut(label: String, thunk: => Unit, suffix: String = "scala") = {
    val output = new ByteArrayOutputStream()
    scala.Console.withOut(new PrintStream(output)) {
      thunk
    }
    check(label, output.toString(), suffix = suffix)
  }
  def check(label: String, raw_code: String, suffix: String = "scala") = {
    val fileprefix = prefix+under+label
    val name = fileprefix+".check."+suffix
    val aname = fileprefix+".actual."+suffix
    val expected = readFile(name)
    val code = indent(raw_code)
    if (expected != code) {
      val wname = if (overwriteCheckFiles) name else aname
      println("writing " + wname)
      writeFile(wname, code)
    } else {
      val f = new File(aname)
      if (f.exists) f.delete
    }
    if (!overwriteCheckFiles) {
      assert(expected == code, name)
    }
  }
  def exec(label: String, code: String, suffix: String = "scala") = {
    val fileprefix = prefix+under+label
    val aname = fileprefix+".actual."+suffix
    utils.writeFileIndented(aname, code)
  }
}
