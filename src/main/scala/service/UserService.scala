package service

import domain.User
import scalaz.zio.ZIO

trait UserModule {

  def user: UserModule.Service

}

object UserModule {

  trait Service {

    def createUser(user: User): ZIO[Any, Nothing, User]

  }

}

object UserLive extends UserModule.Service {

  def createUser(user: User): ZIO[Any, Nothing, User] = ???

}
