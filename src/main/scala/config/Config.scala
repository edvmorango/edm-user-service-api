package config

final case class Config(app: AppConfig, aws: AWSConfig)

final case class AppConfig(host: String, context: String, port: Int)

final case class AWSConfig(dynamodb: DynamoDBConfig)

final case class DynamoDBConfig(prefix: String,
                                region: String,
                                host: String,
                                port: Int,
                                parallelism: Int,
                                accessKey: String,
                                secretKey: String)

object ConfigLoader {
  import java.nio.file.Path
  import pureconfig.generic.auto._
  import scala.util.Try
  import pureconfig.module.yaml._

  def load: Either[Throwable, Config] = {

    val path = Path of ClassLoader.getSystemResource("application.yaml").getPath

    Try(loadYamlOrThrow[Config](path)).toEither

  }

}
