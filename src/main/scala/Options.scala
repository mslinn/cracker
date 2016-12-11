import com.amazonaws.services.polly.model.VoiceId
import com.micronautics.options._
import java.io.File
import org.apache.commons.io.FileUtils

class Options(val args: Array[String]) extends AbstractOptions(args, Options.helpMsg) {
  lazy val (temporaryOut, mp3File)= value("-o").map { outputFileName =>
    val file = new File(outputFileName)
    false -> file
  }.getOrElse(true -> File.createTempFile("cracker", ".mp3"))

  lazy val voiceId: String = value("-w").map { voiceName =>
    if (!Options.voiceNames.contains(voiceName)) throw new InvalidVoiceIdException(s"Voice $voiceName is not valid.")
    voiceName
  }.getOrElse(VoiceId.Joanna.name)

  // This option test must be last because it can reference args(0)
  lazy val message: String = value("-i").map { fileName =>
    FileUtils.readFileToString(new File(fileName), null.asInstanceOf[String]).replaceAll("\\s+", " ")
  }.getOrElse(if (options.nonEmpty) args(0) else "Polly wants a cracker!")
}

object Options {
  lazy val voiceNames: List[String] = VoiceId.values.map(_.name).sorted.toList

  lazy val helpMsg: String =
    s"""You can run this program from SBT:
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
       |VoiceIds are: ${ voiceNames.mkString(", ") }
       |
       |This help message is produced in response to the `-h` option:
       |
       |    $$ java -jar target/scala-2.12/cracker-assembly-0.1.1.jar -h
       |""".stripMargin
}
