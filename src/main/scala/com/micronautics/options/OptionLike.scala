package com.micronautics.options

object OptionLike {
  val testing: Boolean = try {
    throw new Exception("")
  } catch {
    case e: Exception =>
      val x = e.getStackTrace.exists(_.getClassName.startsWith("org.scalatest"))
      x
  }
}

trait OptionLike {
  def args: Array[String]
  def helpMsg: String

  protected var options: Array[String] = args

  if (isPresent("-h")) help("")

  def help(msg: String): Unit = {
    if (msg.nonEmpty) println(s"Error: $msg\n")
    println(helpMsg)
    if (OptionLike.testing) throw new OptionParseException(msg) else System.exit(1)
  }

  def isPresent(option: String): Boolean = {
    val result = options.contains(option)
    options = options.filterNot(_==option)
    result
  }

  def value(option: String): Option[String] = {
    val i: Int = options.indexOf(option)
    if (i>=0) {
      if (options.length<i+2) help(s"The $option option requires a value")
      val result: String = options(i+1)
      options = options.take(i) ++ options.drop(i+2)
      Some(result)
    } else None
  }
}

class OptionParseException(e: String) extends Exception(e)
class InvalidVoiceIdException(e: String) extends Exception(e)
