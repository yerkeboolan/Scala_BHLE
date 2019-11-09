package Serializers


import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}
import model.{SuccessfulResponse, ErrorResponse, Subject, Topic}

trait SprayJsonSerializer extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val successfulResponse: RootJsonFormat[SuccessfulResponse] = jsonFormat2(SuccessfulResponse)
  implicit val errorResponse: RootJsonFormat[ErrorResponse] = jsonFormat2(ErrorResponse)

  implicit val subjectFormat: RootJsonFormat[Subject] = jsonFormat2(Subject)
  implicit val topicFormat: RootJsonFormat[Topic] = jsonFormat3(Topic)

}
