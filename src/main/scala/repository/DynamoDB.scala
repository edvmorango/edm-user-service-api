package repository

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.alpakka.dynamodb.impl.DynamoSettings
import akka.stream.alpakka.dynamodb.scaladsl.DynamoClient
import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import config.AWSConfig

trait DynamoDB {

  val dynamoDB: DynamoDB.Client

}

object DynamoDB {

  trait Client {

    def instance: DynamoClient

  }

  implicit val system: ActorSystem = ActorSystem("scanamo-alpakka")
  implicit val materializer: ActorMaterializer =
    ActorMaterializer.create(system)

  def instantiate(config: AWSConfig): Client = new Client {

    override def instance: DynamoClient = {

      val credentialsProvider = new AWSStaticCredentialsProvider(
        new BasicAWSCredentials(config.dynamodb.accessKey,
                                config.dynamodb.secretKey))

      DynamoClient(
        DynamoSettings(region = config.dynamodb.region,
                       host = config.dynamodb.host,
                       port = config.dynamodb.port,
                       parallelism = config.dynamodb.parallelism,
                       credentialsProvider))

    }
  }

}
