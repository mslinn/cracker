import java.io.File
import java.io.InputStream

object Cracker extends App {
  val options = new Options(args)

  val billedLen = billedLength(options.message)
  if (billedLen>1500 || options.message.length>3000) {
    System.err.println(
      s"""Error: AWS Polly only supports messages up to 3000 characters long, of which no more than 1500 can be billed characters.
         |SSML tags are not counted as billed characters.
         |Your message has $billedLen billed characters and a total of ${ options.message.length } characters.
         |""".stripMargin.replaceAll("\\s+", " "))
    System.exit(2)
  }
  val stream = speechStream(options.message)
  saveMp3File(stream, options.mp3File.getAbsolutePath)
  if (options.temporaryOut) playFile(options.mp3File)

  def billedLength(string: String): Int = {
    val billedString = string.replaceAll("<(\\S+)>.*?</\\1>", "")
    billedString.length
  }

  /** Obtain MP3 stream from AWS Polly that voices the message */
  def speechStream(message: String): InputStream = {
    import com.amazonaws.services.polly._
    import com.amazonaws.services.polly.model._

    val pollyClient = AmazonPollyClientBuilder.standard.build
    val request = new SynthesizeSpeechRequest
    request.setVoiceId(options.voiceId)
    request.setOutputFormat(OutputFormat.Mp3)
    request.setText(message)
    val synthesizeSpeechResult: SynthesizeSpeechResult = pollyClient.synthesizeSpeech(request)
    synthesizeSpeechResult.getAudioStream
  }

  /** Side effect: Create temporary MP3 file from input MP3 stream */
  def saveMp3File(mp3Stream: InputStream, mp3FileName: String): Unit = {
    import java.io.{BufferedInputStream, FileOutputStream}

    val bis = new BufferedInputStream(mp3Stream)
    val fos = new FileOutputStream(mp3FileName)
    val buf = new Array[Byte](1024)
    Iterator
      .continually(bis.read(buf))
      .takeWhile(_ != -1)
      .foreach(fos.write(buf, 0, _))
  }

  /** Side effects: Play mp3 file using javafx and delete mp3 file
    * The shutdown technique is explained in https://scalacourses.com/student/showLecture/175 */
  def playFile(mp3File: File): Unit = {
    import javafx.scene.media.{Media, MediaPlayer}

    com.sun.javafx.application.PlatformImpl.startup( () => {
      val media = new Media(mp3File.toURI.toString)
      val mediaPlayer: MediaPlayer = new MediaPlayer(media)
      mediaPlayer.play()
      mediaPlayer.setOnEndOfMedia { () =>
          mp3File.delete()
          System.exit(0)
      }
    })
  }
}
