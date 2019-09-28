package FilmsAndDirectors

object Boot extends App {

  case class Film(name: String,
                  yearOfRelease: Int,
                  imdbRating: Double)

  case class Director(firstName: String,
                      lastName: String,
                      yearOfBirth: Int,
                      films: Seq[Film])


  val memento = new Film("Memento", 2000, 8.5)
  val darkKnight = new Film("Dark Knight", 2008, 9.0)
  val inception = new Film("Inception", 2010, 8.8)
  val highPlainsDrifter = new Film("High Plains Drifter", 1973, 7.7)
  val outlawJoseyWales = new Film("The Outlaw Josey Wales", 1976, 7.9)
  val unforgiven = new Film("Unforgiven", 1992, 8.3)
  val granTorino = new Film("Gran Torino", 2008, 8.2)
  val invictus = new Film("Invictus", 2009, 7.4)
  val predator = new Film("Predator", 1987, 7.9)
  val dieHard = new Film("Die Hard", 1988, 8.3)
  val huntForRedOctober = new Film("The Hunt for Red October", 1990, 7.6)
  val thomasCrownAffair = new Film("The Thomas Crown Affair", 1999, 6.8)
  val eastwood = new Director("Clint", "Eastwood", 1930,
    Seq(highPlainsDrifter, outlawJoseyWales, unforgiven, granTorino, invictus))
  val mcTiernan = new Director("John", "McTiernan", 1951,
    Seq(predator, dieHard, huntForRedOctober, thomasCrownAffair))
  val nolan = new Director("Christopher", "Nolan", 1970,
    Seq(memento, darkKnight, inception))
  val someGuy = new Director("Just", "Some Guy", 1990,
    Seq())
  val directors = Seq(eastwood, mcTiernan, nolan, someGuy)

  // Task 1
  val a: Int => Seq[Director] = (numberOfFilms: Int) => directors.filter(d => d.films.size > numberOfFilms)
  println("Task 1: " + a(3))

    // Task 2
  val b = (year: Int) => directors.filter(d => d.yearOfBirth < year)
  println("Task 2: " + b(1998))

    // Task 3
  val c = (numberOfFilms: Int, year : Int) => directors.filter(d => d.films.size > numberOfFilms && d.yearOfBirth < year)
  println("Task 3: " + c(4, 1998))

  // Task 4
    val d = (ascending: Boolean) => ascending match{
      case true => directors.sortWith(_.yearOfBirth < _.yearOfBirth)
      case false => directors.sortWith(_.yearOfBirth > _.yearOfBirth)
    }
  println("Task 4: " + d(true))

    // Task 5
  val e = nolan.films.map(f => f.name)
  println("Task 5: " + e)

    // Task 6
  val f = directors.flatMap(d => d.films.map(f => f.name))
  println("Task 6: " + f)

    // Task 7
  val g = mcTiernan.films.map(f => f.yearOfRelease).min
  println("Task 7: " + g)

    // Task 8
  val h = directors.flatMap(d => d.films).sortWith(_.imdbRating < _.imdbRating)
  println("Task 8: " + h)

    // Task 9
    def i() : Double = {
      val films: Seq[Film] = directors.flatten(director => director.films)
      val sum: Double = films.foldLeft(0.0)((sum, film) => sum + film.imdbRating)
      sum / films.size
    }

  println("Task 9: " + i)

    // Task 10

  val k = directors.foreach(d => d.films.foreach(f => println(s"Tonight only! ${f.name} by ${d.firstName}")))

  println("Task 10: " + k)


    // Task 11
  def l(): Option[Film] = {
    val res= directors.flatMap(d => d.films).sortWith((f1, f2) => (f1.yearOfRelease) < f2.yearOfRelease)
    res.headOption
  }

  println("Task 11: " + l)
}

