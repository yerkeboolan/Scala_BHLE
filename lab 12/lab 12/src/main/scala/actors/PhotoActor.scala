package actors

import java.io.{File, InputStream}
import com.amazonaws.util.IOUtils
import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.{GetObjectRequest, ObjectMetadata, PutObjectRequest, S3Object, S3ObjectInputStream}
import models.{ErrorResponse, PhotoResponse,SuccessResponse}


object PhotoActor{
  case class UploadPhoto(inputStream: InputStream, fileName:String, contentType: String)
  case class DownloadPhoto(name:String)
  def props(client: AmazonS3, bucketName:String) = Props(new PhotoActor(client, bucketName))
}

class PhotoActor(client:AmazonS3, bucketName: String) extends  Actor with ActorLogging {
  import  PhotoActor._

  override def receive: Receive = {
    case UploadPhoto(inputStream,fileName,contentType) =>

      val metadata = new ObjectMetadata()
      val key = s"photo/$fileName"
      metadata.setContentType(contentType)
      val request = new PutObjectRequest(bucketName,key,inputStream,metadata)
      val result = client.putObject(request)
      sender() ! SuccessResponse(201, s"file version: ${result.getVersionId}")

      log.info("Successfully put objet with filename: {} to AWS S3", fileName)

      context.stop(self)

    case DownloadPhoto(name) =>

      if(client.doesObjectExist(bucketName, name)){
        val contentType = client.getObject(new GetObjectRequest(bucketName, name)).getObjectMetadata.getContentType
        val objectContent: S3ObjectInputStream  = client.getObject(new GetObjectRequest(bucketName, name)).getObjectContent
        val bytePhotos: Array[Byte] = IOUtils.toByteArray(objectContent)
        log.info(s"Successfully found photo with name: ${name}")
        sender() ! Right(PhotoResponse(200,bytePhotos))
      }else{

        log.info(s"Photo with name: ${name} doesn't exist")
        sender() ! Left(ErrorResponse(404, s"Photo with name: ${name} doesn't exist"))
      }
      context.stop(self)



  }
}
