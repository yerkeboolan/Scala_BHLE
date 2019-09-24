package ShapingUp

object Draw {
  def apply(shape: Shape) = shape match {
    case Circle(radius) =>{
      val circle = Circle.circle(radius)
      s"A circle of radius ${radius}cm"
    }
    case Rectangle(width, height) => {
      val rectangle = Rectangle.rectangle(width, height)
      s"A rectangle of width ${width}cm and height ${height}cm"
    }
    case Square(side) => {
      val square = Square.square(side)
      s"A square of side ${side}cm"
    }
  }
}
