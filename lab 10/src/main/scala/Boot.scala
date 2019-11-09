
import akka.actor.ActorSystem
import akka.pattern.ask
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.{delete, put, _}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.stream.{ActorMaterializer, Materializer}
import akka.util.Timeout

import Serializers.SprayJsonSerializer
import actor.SubjectTopicManager
import model.{SuccessfulResponse, ErrorResponse, Topic}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContextExecutor


object Boot extends App with SprayJsonSerializer {

  implicit val system: ActorSystem = ActorSystem("topic-service")
  implicit val materializer: Materializer = ActorMaterializer()
  implicit val ec: ExecutionContextExecutor = system.dispatcher
  implicit val timeout: Timeout = Timeout(10.seconds)

  val topicManager = system.actorOf(SubjectTopicManager.props(), "topic-manager")


  val route =
    pathPrefix("subject") {
      path("topic" / Segment) { topicId =>
        concat(
          get {
            complete {
              (topicManager ? SubjectTopicManager.ReadTopic(topicId)).mapTo[Either[ErrorResponse, Topic]]
            }
          },
          delete {
            complete {
              (topicManager ? SubjectTopicManager.DeleteTopic(topicId)).mapTo[Either[ErrorResponse, SuccessfulResponse]]
            }
          }
        )
      } ~
        path("topic") {
          concat(
            post {
              entity(as[Topic]) { topic =>
                complete {
                  (topicManager ? SubjectTopicManager.CreateTopic(topic)).mapTo[Either[ErrorResponse, SuccessfulResponse]]
                }
              }
            },
            put {
              entity(as[Topic]) { topic =>
                complete {
                  (topicManager ? SubjectTopicManager.UpdateTopic(topic)).mapTo[Either[ErrorResponse, SuccessfulResponse]]
                }
              }
            }
          )
        }
    }

  val bindingFuture = Http().bindAndHandle(route, "0.0.0.0", 8080)
}