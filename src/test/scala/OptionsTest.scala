import org.scalatest._
import org.scalatest.OptionValues._

class OptionsTest extends WordSpec with Matchers {
  "Options" should {
    "work" in {
      val options1 = new Options(Array.empty, testing=true)
      options1.value("-i") shouldBe None
      options1.value("-o") shouldBe None

      val options2 = new Options(Array("-i"), testing=true)
      intercept[OptionParseException] { options2.value("-i") }

      val options3 = new Options(Array("-i", "input.txt"), testing=true)
      options3.value("-i").value shouldBe "input.txt"
      options3.value("-o") shouldBe None

      val options4 = new Options(Array("-i", "input.txt", "-o", "output.mp3"), testing=true)
      options4.value("-i").value shouldBe "input.txt"
      options4.value("-o").value shouldBe "output.mp3"

      intercept[OptionParseException] { new Options(Array("-h"), testing=true) }
    }
  }
}
