import com.amazonaws.services.polly._
import com.amazonaws.services.polly.model._

object Cracker extends App {
  val pollyClient = AmazonPollyClientBuilder.standard.build
  val request = new SynthesizeSpeechRequest
  request.setVoiceId(VoiceId.Joanna)
  request.setOutputFormat(OutputFormat.Mp3)
  request.setText("Polly wants a cracker!")
  pollyClient.synthesizeSpeech(request)
}
