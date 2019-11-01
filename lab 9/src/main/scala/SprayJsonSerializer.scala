import spray.json.{DefaultJsonProtocol, RootJsonFormat}
import model.{Subject, ErrorResponse, Topic, SuccessfulResponse}

// DefaultJsonProtocol is responsible for default formats:
// Int, Double, String, ....

trait SprayJsonSerializer extends DefaultJsonProtocol {
  // custom formats
  implicit val directorFormat: RootJsonFormat[Subject] = jsonFormat4(Director)
  implicit val movieFormat: RootJsonFormat[Movie] = jsonFormat4(Movie)

  implicit val successfulResponse: RootJsonFormat[SuccessfulResponse] = jsonFormat2(SuccessfulResponse)
  implicit val errorResponse: RootJsonFormat[ErrorResponse] = jsonFormat2(ErrorResponse)
}