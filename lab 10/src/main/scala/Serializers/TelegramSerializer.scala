package Serializers

import model.TelegramMessage
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait TelegramSerializer extends DefaultJsonProtocol with SprayJsonSerializer {
  implicit val messageFormat: RootJsonFormat[TelegramMessage] = jsonFormat2(TelegramMessage)
}
