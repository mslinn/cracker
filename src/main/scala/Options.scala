import java.io.File
import com.amazonaws.services.polly.model.VoiceId

class Options(args: Array[String], testing: Boolean=false) {
  protected var options: Array[String] = args

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

  if (isPresent("-h")) help("")

  lazy val (temporaryOut, mp3File)= value("-o").map { outputFileName =>
    val file = new File(outputFileName)
    false -> file
  }.getOrElse(true -> File.createTempFile("cracker", ".mp3"))

  lazy val voiceIds: List[String] = VoiceId.values.map(_.name).toList
  lazy val voiceId: String = value("-w").map { vId =>
    if (!voiceIds.contains(vId)) throw new InvalidVoiceIdException(s"Voice $vId is not valid.")
    vId
  }.getOrElse(VoiceId.Joanna.name)

  // This option test must be last because it can reference args(0)
  lazy val message: String = value("-i").map { fileName =>
    io.Source.fromFile(fileName).mkString.replaceAll("\\s+", " ")
  }.getOrElse(if (options.nonEmpty) args(0) else "Polly wants a cracker!")

  def help(msg: String): Unit = {
    if (msg.nonEmpty) println(s"Error: $msg\n")
    println(s"""You can run this program from SBT:
               |
               |    sbt run
               |
               |You can also build a fat jar and run it with arbitrary message to voice:
               |
               |    $$ sbt assembly
               |    $$ java -jar target/scala-2.12/cracker-assembly-0.1.0.jar \\
               |    "Learn Scala and Play at ScalaCourses.com!"
               |
               |By default, the program creates a temporary MP3 file, plays it, then deletes it.
               |You can specify that the MP3 file should be kept by giving it a name with the `-o` option.
               |
               |    $$ java -jar target/scala-2.12/cracker-assembly-0.1.0.jar \\
               |    -o blah.mp3 \\
               |    "Learn Scala and Play at ScalaCourses.com!"
               |
               |You can also cause input to be read with the `-i` option; this causes any input provided on the command line to be ignored.
               |
               |    $$ java -jar target/scala-2.12/cracker-assembly-0.1.0.jar \\
               |    -i input.txt \\
               |    -o blah.mp3
               |
               |The voice used defaults to Joanna, but you can override this with the -w option.
               |VoiceIds are: ${ voiceIds.mkString(", ") }
               |""".stripMargin)
    if (testing) throw new OptionParseException(msg) else System.exit(1)
  }
}

class OptionParseException(e: String) extends Exception(e)
class InvalidVoiceIdException(e: String) extends Exception(e)
