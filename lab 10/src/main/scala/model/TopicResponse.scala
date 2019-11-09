package model

case class TopicResponse(topic: Topic, isSuccessful: Boolean, statusCode: Int, message: String)
