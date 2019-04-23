package service

import domain.User
import interop.FUuid
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
}

object UserServiceImpl extends Service[UserRepository] {

  override def createUser(user: User): ZIO[UserRepository, Throwable, Unit] = {

    ZIO.accessM[UserRepository] { env =>
      for {
        userEmail <- env.userRepository.findByEmail(user.email)
        _ <- userEmail match {
          case Some(_) =>
            ZIO.fail(new Exception(s"Email ${user.email} already exists."))
          case None => ZIO.unit
        }
        uuid <- FUuid.getUUID()
        _ <- env.userRepository createUser user.copy(uuid = Option(uuid))
      } yield ()

    }

  }

  override def findByUuid(
      uuid: String): ZIO[UserRepository, Throwable, Option[User]] =
    ZIO.accessM(_.userRepository.findByUuid(uuid))

}
