package actor

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, RequestEntity}
import akka.stream.{ActorMaterializer, Materializer}
import com.sksamuel.elastic4s.ElasticsearchClientUri
import com.sksamuel.elastic4s.http.ElasticDsl._
import com.sksamuel.elastic4s.http.HttpClient
import model.{ErrorResponse, Subject, SuccessfulResponse, TelegramManager, TelegramMessage, Topic, TopicResponse}
import Serializers.{ElasticSerializer, SprayJsonSerializer}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

object SubjectTopicManager {

  case class CreateTopic(recipe: Topic)

  case class ReadTopic(id: String)

  case class UpdateTopic(recipe: Topic)

  case class DeleteTopic(id: String)

  def props() = Props(new SubjectTopicManager)

}

class SubjectTopicManager extends Actor with ActorLogging with ElasticSerializer {
  import SubjectTopicManager._

  implicit val system: ActorSystem = ActorSystem("telegram-service")
  implicit val materializer: Materializer = ActorMaterializer()
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  val client = HttpClient(ElasticsearchClientUri("localhost", 9200))

  val chat_id = -352088280

  override def receive: Receive = {
    case CreateTopic(topic:Topic)  =>
      val cmd = client.execute(indexInto("topics" / "_doc").id(topic.id).doc(topic))
      val replyTo = sender()

      cmd.onComplete {
        case Success(_) =>
          val topicResponse = TopicResponse(null, isSuccessful = true, 201, s"Topic with ID = ${topic.id} successfully created. Topic = [$topic]")
          handleResponse(replyTo, topicResponse)
          var msg = TelegramMessage(chat_id, s"Topic with ID = ${topic.id} successfully created. Topic = [$topic]")
          TelegramManager(msg)

        case Failure(_) =>
          val topicResponse = TopicResponse(null, isSuccessful = false, 409, s"Bad response")
          handleResponse(replyTo, topicResponse)
      }

    case ReadTopic(id) =>
      val cmd = client.execute(get(id).from("topics" / "_doc"))
      val replyTo = sender()

      cmd.onComplete {
        case Success(either) => either match {
          case Right(right) =>
            if (right.result.found) {
              either.map(e => e.result.to[Topic]).foreach { recipe =>
                val topicResponse = TopicResponse(recipe, isSuccessful = true, 201, null)
                handleResponse(replyTo, topicResponse)
                var msg = TelegramMessage(chat_id, s"Topic with ${id} found")
                TelegramManager(msg)
              }
            } else {
              val topicResponse = TopicResponse(null, isSuccessful = false, 404, s"Topic with id = $id not found")
              handleResponse(replyTo, topicResponse)
            }

          case Left(_) =>
            val topicResponse = TopicResponse(null, isSuccessful = false, 500, s"Elastic Search internal error")
            handleResponse(replyTo, topicResponse)
        }

        case Failure(exception) =>
          val topicResponse = TopicResponse(null, isSuccessful = false, 401, exception.getMessage)
          handleResponse(replyTo, topicResponse)
      }

    case UpdateTopic(topic) =>
      val cmd = client.execute(update(topic.id).in("topics" / "_doc").docAsUpsert(topic))
      val replyTo = sender()

      cmd.onComplete {
        case Success(_) =>
          val topicResponse = TopicResponse(null, isSuccessful = true, 201, s"Topic with ID = ${topic.id} successfully updated. Recipe = [$topic]")
          handleResponse(replyTo, topicResponse)
          var msg = TelegramMessage(chat_id, s"Topic with ID = ${topic.id} successfully updated. Topic = [$topic]")
          TelegramManager(msg)

        case Failure(_) =>
          val topicResponse = TopicResponse(null, isSuccessful = false, 409, s"Bad response")
          handleResponse(replyTo, topicResponse)
      }

    case DeleteTopic(id) =>
      val cmd = client.execute(delete(id).from("topics" / "_doc"))
      val replyTo = sender()

      cmd.onComplete {
        case Success(either) => either match {
          case Right(right) =>
            if (right.result.found) {
              val topicResponse = TopicResponse(null, isSuccessful = true, 201, s"Topic with ID = $id successfully deleted.")
              handleResponse(replyTo, topicResponse)
              var msg = TelegramMessage(chat_id, s"Topic with ID = ${id} successfully deleted.")
              TelegramManager(msg)



            } else {
              val topicResponse = TopicResponse(null, isSuccessful = false, 409, s"Topic  with id = $id not found.")
              handleResponse(replyTo, topicResponse)
            }
          case Left(_) =>
            val topicResponse = TopicResponse(null, isSuccessful = false, 500, s"Elastic Search internal error")
            handleResponse(replyTo, topicResponse)
        }
        case Failure(exception) =>
          val topicResponse = TopicResponse(null, isSuccessful = false, 401, exception.getMessage)
          handleResponse(replyTo, topicResponse)
      }
  }

  private def handleResponse(replyTo: ActorRef, topicResponse: TopicResponse): Unit = {
    if (topicResponse.isSuccessful) {
      if (topicResponse.topic != null) {
        replyTo ! Right(topicResponse.topic)
      } else {
        replyTo ! Right(SuccessfulResponse(topicResponse.statusCode, topicResponse.message))
      }
    } else {
      replyTo ! Left(ErrorResponse(topicResponse.statusCode, topicResponse.message))
    }
  }
}