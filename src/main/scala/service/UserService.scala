package service

import domain.User
import repository.UserRepository
import scalaz.zio.ZIO
import service.UserModule.Service

trait UserModule {

  def user: UserModule.Service[UserRepository]

}

object UserModule {

  trait Service[R] {

    def createUser(user: User): ZIO[R, Throwable, Unit]

    def findByUuid(uuid: String): ZIO[R, Throwable, Option[User]]

  }
//
//  def service[R <: UserRepository[R]](
//      userRepository: UserRepository[]): Service[R] = new Service[R] {
//
//    override def createUser(user: User): ZIO[R, Throwable, Unit] = ZIO.accessM(_.createUser(user))
//
//    override def findByUuid(uuid: String): ZIO[R, Throwable, Option[User]] = ZIO.accessM(_.findByUuid(uuid))
//
//  }

}

object UserServiceImpl extends Service[UserRepository] {

  override def createUser(user: User): ZIO[UserRepository, Throwable, Unit] =
    ???

  override def findByUuid(
      uuid: String): ZIO[UserRepository, Throwable, Option[User]] = ???

}
