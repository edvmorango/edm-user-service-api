package service

import scalaz.zio.ZIO

trait User {

  def user: User.Service

}

object User {

  trait Service {

    def createUser(user: User): ZIO[Any, Nothing, Unit]

  }

}

object UserLive extends User.Service {

  def createUser(user: User): ZIO[Any, Nothing, Unit] = ???

}
