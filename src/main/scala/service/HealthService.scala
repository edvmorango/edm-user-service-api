package service

import scalaz.zio.ZIO

trait Health {

  def health: Health.Service

}

object Health {

  trait Service {

    def showStatus(line: String): ZIO[Any, Nothing, Unit]

  }

}

object HealthLive extends Health.Service {

  def showStatus(line: String): ZIO[Any, Nothing, Unit] = ???
}
