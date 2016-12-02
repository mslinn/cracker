object Cracker extends App {
  import java.io.InputStream

  val mp3FileName = java.io.File.createTempFile("cracker", ".mp3").getAbsolutePath
  val message = if (args.nonEmpty) args(0) else "Polly wants a cracker!"
  val stream = speechStream(message)
  saveMp3File(stream, mp3FileName)
  playFile(mp3FileName)

  /** Obtain MP3 stream from AWS Polly that voices the message */
  def speechStream(message: String): InputStream = {
    import com.amazonaws.services.polly._
    import com.amazonaws.services.polly.model._

    val pollyClient = AmazonPollyClientBuilder.standard.build
    val request = new SynthesizeSpeechRequest
    request.setVoiceId(VoiceId.Joanna)
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
  def playFile(mp3FileName: String): Unit = {
    import javafx.scene.media.{Media, MediaPlayer}
    import scala.concurrent.Promise

    com.sun.javafx.application.PlatformImpl.startup( () => {
      val file = new java.io.File(mp3FileName)
      val uri = file.toURI
      val media = new Media(uri.toString)
      val mediaPlayer: MediaPlayer = new MediaPlayer(media)
      mediaPlayer.play()
      val promise = Promise[String]()
      mediaPlayer.setOnEndOfMedia { () =>
        promise.success("done")
        file.delete()
        System.exit(0)
      }
    })
  }
}
