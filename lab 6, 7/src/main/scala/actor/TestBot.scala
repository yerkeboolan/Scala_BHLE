package actor

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import model.{Director, ErrorResponse, Movie, SuccessfulResponse}

object TestBot {

  case object TestCreate

  case object TestConflict

  case object TestUpdate

  case object TestDelete

  case object TestRead

  case object TestNotFound

  def props(manager: ActorRef) = Props(new TestBot(manager))
}

class TestBot(manager: ActorRef) extends Actor with ActorLogging {
  import TestBot._

  override def receive: Receive = {
    case TestCreate =>
      manager ! MovieManager.CreateMovie(Movie("1", "Joker", Director("dir-1", "Todd", None, "Philips"), 2019))

    case TestConflict =>
      manager ! MovieManager.CreateMovie(Movie("2", "Charlie's Angels", Director("dir-2", "Ivan", None, "Ivanov"), 2019))
      manager ! MovieManager.CreateMovie(Movie("2", "Test Test", Director("dir-2", "Ivan", None, "Ivanov"), 2019))

    case TestRead =>
      manager ! MovieManager.ReadMovie("1")

    case TestNotFound =>
    // FIXME: implement me

    case SuccessfulResponse(status, msg) =>
      log.info("Received Successful Response with status: {} and message: {}", status, msg)

    case ErrorResponse(status, msg) =>
      log.warning("Received Error Response with status: {} and message: {}", status, msg)

    case movie: Movie =>
      log.info("Received movie: [{}]", movie)


    case TestUpdate =>
      manager ! MovieManager.UpdateMovie(Movie("1", "Charlie's Angels", Director("dir-2", "Ivan", None, "Ivanov"), 2019))

    case TestDelete =>
      manager ! MovieManager.DeleteMovie("1")

    case TestNotFound =>
      manager ! MovieManager.ReadMovie("4848")
      manager ! MovieManager.DeleteMovie("4848")

    // TODO: add tests
  }
}