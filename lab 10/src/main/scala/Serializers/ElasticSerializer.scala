package Serializers

import com.sksamuel.elastic4s.{Hit, HitReader, Indexable}
import spray.json._
import scala.util.Try
import model.Topic

trait ElasticSerializer extends SprayJsonSerializer  {

  implicit object TopicIndexable extends Indexable[Topic] {
    override def json(topic: Topic): String = topic.toJson.compactPrint
  }

  implicit object TopicHitReader extends HitReader[Topic] {
    override def read(hit: Hit): Either[Throwable, Topic] = {
      Try {
        val json = hit.sourceAsString.parseJson
        json.convertTo[Topic]
      }.toEither
    }
  }
}
