package ShapingUp

trait Rectangular {

  this: Shape =>

  override def sides(): Int = 4
}
