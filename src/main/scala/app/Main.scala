package app

import config.ConfigLoader
import endpoint.HealthEndpoint
import scalaz.zio.console.Console
import scalaz.zio.{App, Task, TaskR, ZIO}
import scalaz.zio.console._
import org.http4s.server.Router
import org.http4s.implicits._
import scalaz.zio.interop.catz._

object Main extends App {

  type Env = Console
  type App[A] = TaskR[Env, A]

  def createRoutes[F[_]](basePath: String) = {

    val healthEndpoints = new HealthEndpoint().endpoints

    val endpoints = healthEndpoints

    Router[App](basePath -> endpoints).orNotFound

  }

  override def run(args: List[String]): ZIO[Env, Nothing, Int] = {
    val program =
      for {
        cfg <- ZIO.fromEither(ConfigLoader.load)
        _ <- putStr(cfg.toString)
        routes <- createRoutes(cfg.app.context)

      } yield 1

    program.foldM(e => putStrLn(e.getMessage) *> ZIO.succeed(0),
                  _ => ZIO.succeed(0))
  }

}
