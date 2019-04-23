package app

import cats.effect.ExitCode
import cats.syntax.all._
import config.ConfigLoader
import endpoint.{HealthEndpoint, UserEndpoint}
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import repository.{DynamoDB, UserRepository, UserRepositoryDynamoDB}
import scalaz.zio.clock.Clock
import scalaz.zio.console.{Console, _}
import scalaz.zio.interop.catz._
import scalaz.zio.scheduler.Scheduler
import scalaz.zio.{App, TaskR, ZIO}
import service.{Health, HealthLive, UserModule, UserServiceImpl}

object Main extends App {

  type AppEnvironment = Console with Clock with Health with UserModule
  type AppTask[A] = TaskR[AppEnvironment, A]

  def createRoutes(basePath: String) = {

    println(basePath)
    val healthEndpoints =
      new HealthEndpoint[AppEnvironment]("health").endpoints

    val userEndpoints =
      new UserEndpoint[AppEnvironment]("user").endpoints

    val endpoints = healthEndpoints <+> userEndpoints

    Router[AppTask](basePath -> endpoints).orNotFound

  }

  override def run(args: List[String]): ZIO[Environment, Nothing, Int] = {
    val program =
      for {
        cfg <- ZIO.fromEither(ConfigLoader.load)
        dynamoDB: DynamoDB.Client = DynamoDB.instantiate(cfg.aws)
        httpApp = createRoutes(cfg.app.context)
        server <- ZIO
          .runtime[AppEnvironment]
          .flatMap { implicit rts =>
            BlazeServerBuilder[AppTask]
              .bindHttp(cfg.app.port, "0.0.0.0")
              .withHttpApp(httpApp)
              .serve
              .compile[AppTask, AppTask, ExitCode]
              .drain
          }
          .provideSome[Environment] { base =>
            new Console with Clock with Health with UserRepository
            with UserModule {

              override val scheduler: Scheduler.Service[Any] = base.scheduler
              override val console: Console.Service[Any] = base.console
              override val clock: Clock.Service[Any] = base.clock

              override def health: Health.Service = HealthLive

              override val userRepository: UserRepository.Repository[Any] =
                new UserRepositoryDynamoDB(dynamoDB)

              override def user: UserModule.Service[UserRepository] =
                UserServiceImpl
            }
          }
      } yield server

    program.foldM(e => putStrLn(e.getMessage) *> ZIO.succeed(1),
                  _ => ZIO.succeed(0))
  }

}
