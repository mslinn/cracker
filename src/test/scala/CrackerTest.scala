import org.scalatest._

class CrackerTest extends WordSpec with Matchers {
  "Cracker billed length" should {
    "exclude xml" in {
      Cracker.billedLength("blah") === 4
      Cracker.billedLength("blah<i>asdf</i>") === 4
    }
  }
}
