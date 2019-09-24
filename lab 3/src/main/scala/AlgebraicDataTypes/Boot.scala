package AlgebraicDataTypes

object Boot extends App {

  sealed trait IntCalculator
  case class Sum(a: Int, b: Int) extends IntCalculator
  case class Subtract(a: Int, b: Int) extends IntCalculator
  case class Multiple(a: Int, b: Int) extends IntCalculator
  case class Divide(a: Int, b: Int) extends IntCalculator

  case object Calculator {

    def calculate(operation: IntCalculator) = operation match {
        case Sum(a, b) => a + b
        case Subtract(a, b) => a - b
        case Multiple(a, b) => a * b
        case Divide(a, b) => a / b
    }
  }

  case object Source extends Enumeration {
    type Source = Value
    val well, spring, tap = Value
  }

  import Source._

  case class BottledWater(size: Int, source: Source, carbonated: Boolean)

  val bw = BottledWater(12, Source.spring, true)


}
