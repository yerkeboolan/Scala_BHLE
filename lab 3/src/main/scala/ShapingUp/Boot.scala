package ShapingUp

object Boot extends App {
  println(Draw.apply(Circle(3)))
  println(Draw.apply(Rectangle(9, 8)))
  println(Draw.apply(Square(11)))
}
