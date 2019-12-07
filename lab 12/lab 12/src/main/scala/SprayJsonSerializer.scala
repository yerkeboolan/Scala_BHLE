import spray.json.{DefaultJsonProtocol, RootJsonFormat}
import models.{ErrorResponse, SuccessResponse}

trait SprayJsonSerializer extends DefaultJsonProtocol {

  implicit val successfulFormat: RootJsonFormat[SuccessResponse] = jsonFormat2(SuccessResponse)
  implicit val errorFormat: RootJsonFormat[ErrorResponse] = jsonFormat2(ErrorResponse)
}