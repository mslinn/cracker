import com.micronautics.options.OptionParseException
import org.scalatest._
import org.scalatest.OptionValues._

class OptionsTest extends WordSpec with Matchers {
  "Options" should {
    "handle unspecified switches" in {
      val options = new Options(Array.empty)
      options.value("-i") shouldBe None
      options.value("-o") shouldBe None
    }

    "throw exception for missing switch argument" in {
      val options = new Options(Array("-i"))
      intercept[OptionParseException] { options.value("-i") }
    }

    "return switch value" in {
      val options = new Options(Array("-i", "input.txt"))
      options.value("-i").value shouldBe "input.txt"
      options.value("-o") shouldBe None
    }

    "return switch values" in {
      val options = new Options(Array("-i", "input.txt", "-o", "output.mp3"))
      options.value("-i").value shouldBe "input.txt"
      options.value("-o").value shouldBe "output.mp3"
    }

    "handle help message" in {
      intercept[OptionParseException] { new Options(Array("-h")) }
    }
  }
}
