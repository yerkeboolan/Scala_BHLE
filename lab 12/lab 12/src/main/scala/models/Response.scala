package models

sealed trait Response

case class SuccessResponse(status: Int, message: String) extends Response
case class PhotoResponse(status: Int, message: Array[Byte]) extends Response
case class ErrorResponse(status: Int, message: String) extends Response