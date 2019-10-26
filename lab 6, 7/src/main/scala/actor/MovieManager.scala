package actor
import akka.actor.{Actor, ActorLogging, Props}
import model.{ErrorResponse, Movie, SuccessfulResponse}

// props
// messages
object MovieManager {

  // Create
  case class CreateMovie(movie: Movie)

  // Read
  case class ReadMovie(id: String)

  // Update
  case class UpdateMovie(movie: Movie)

  // Delete
  case class DeleteMovie(id: String)

  def props() = Props(new MovieManager)
}

// know about existing movies
// can create a movie
// can manage movie
class MovieManager extends Actor with ActorLogging {

  // import companion OBJECT
  import MovieManager._

  var movies: Map[String, Movie] = Map()

  override def receive: Receive = {

    case CreateMovie(movie) =>
      movies.get(movie.id) match {
        case Some(existingMovie) =>
          log.warning(s"Could not create a movie with ID: ${movie.id} because it already exists.")
          sender() ! Left(ErrorResponse(409, s"Movie with ID: ${movie.id} already exists."))

        case None =>
          movies = movies + (movie.id -> movie)
          log.info("Movie with ID: {} created.", movie.id)
          sender() ! Right(SuccessfulResponse(201, s"Movie with ID: ${movie.id} created."))
      }

    case msg:ReadMovie =>
      movies.get(msg.id) match {
        case Some(movie) =>
          log.info("Movie with ID: {} was returned", msg.id)
          sender() ! Right(movie)
        case None =>
          log.warning(s"Movie with ID ${msg.id} not found")
          sender()! Left(ErrorResponse(404,s"Movie with ID ${msg.id} not found"))
      }

    case UpdateMovie(movie) =>
      movies.get(movie.id) match {
        case Some(existingMovie) =>
          movies = movies - movie.id
          movies = movies + (movie.id -> movie)

          log.info("Movie with ID: {} updated", movie.id)
          sender()! Right((SuccessfulResponse(200, s"Movie with ID: ${movie.id} was updated")))

        case None =>
          log.warning(s"Movie with ID ${movie.id} not updated because it not found")
          sender()! Left(ErrorResponse(404,s"Movie with ID ${movie.id} not found"))
      }

    case DeleteMovie(id) =>
      movies.get(id) match {
        case Some(existingMovie) =>
          movies = movies - id
          log.info("Movie with ID: {} deleted", id )
          sender() ! Right(SuccessfulResponse(200,s"Movie with ID $id has successfully deleted" ))
        case None=>
          log.warning(s"Movie with ID ${id} not found")
          sender()! Left(ErrorResponse(404,s"Movie with ID ${id} not found"))
      }

    // TODO: UpdateMovie

    // TODO: DeleteMovie
  }

  def randomInt() =
  // FIXME: use random
    4
}