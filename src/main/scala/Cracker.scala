import java.io.{BufferedInputStream, FileOutputStream, InputStream}
import javax.sound.sampled.AudioSystem
import com.amazonaws.services.polly._
import com.amazonaws.services.polly.model._

object Cracker extends App {
  val mp3Name = "cracker.mp3"

  val message = if (args.nonEmpty) args(0) else "Polly wants a cracker!"
  val stream = speechStream(message)
  saveMp3File(stream, mp3Name)
  //playFile(mp3Name) // disabled because it does not work

  def speechStream(message: String): InputStream = {
    val pollyClient = AmazonPollyClientBuilder.standard.build
    val request = new SynthesizeSpeechRequest
    request.setVoiceId(VoiceId.Joanna)
    request.setOutputFormat(OutputFormat.Mp3)
    request.setText(message)
    val synthesizeSpeechResult: SynthesizeSpeechResult = pollyClient.synthesizeSpeech(request)
    synthesizeSpeechResult.getAudioStream
  }

  /** Side effect: Create MP3 file */
  def saveMp3File(stream: InputStream, mp3Name: String): Unit = {
    val bis = new BufferedInputStream(stream)
    val fos = new FileOutputStream(mp3Name)
    val buf = new Array[Byte](1024)
    Iterator
      .continually(bis.read(buf))
      .takeWhile(_ != -1)
      .foreach(fos.write(buf, 0, _))
  }

  /** Side effect: Play mp3 file
    * Exception in thread "main" java.lang.NullPointerException
    	at com.sun.media.sound.SoftMidiAudioFileReader.getAudioInputStream(SoftMidiAudioFileReader.java:134) */
  def playFile(mp3Name: String): Unit = {
    val clip = AudioSystem.getClip()
    val x: InputStream = getClass.getResourceAsStream(mp3Name)
    val audioInputStream = AudioSystem.getAudioInputStream(x)
    clip.open(audioInputStream)
    clip.start()
  }
}
