package service

import domain.User
import repository.UserRepository
import scalaz.zio.ZIO
import service.UserModule.{Service, UserServiceEnvironment}

object UserModule {

  type UserServiceEnvironment = UserRepository with UUID

  trait Service[R] {

    def createUser(user: User): ZIO[R, Throwable, User]

    def findByUuid(uuid: String): ZIO[R, Throwable, Option[User]]

  }

}

object UserServiceImpl extends Service[UserServiceEnvironment] {

  override def createUser(
      user: User): ZIO[UserRepository with UUID, Throwable, User] = {

    ZIO.accessM[UserRepository with UUID] { env =>
      for {
        _ <- ZIO.succeedLazy(println("Before fails"))
        userEmail <- env.userRepository.findByEmail(user.email)
        _ <- userEmail match {
          case Some(_) =>
            ZIO.fail(new Exception(s"Email ${user.email} already exists."))
          case None => ZIO.unit
        }
        uuid <- env.uuidGen.genUUID()
        fuser = user.copy(uuid = Option(uuid))
        _ <- env.userRepository createUser fuser
      } yield fuser

    }

  }

  override def findByUuid(
      uuid: String): ZIO[UserRepository, Throwable, Option[User]] =
    ZIO.accessM(_.userRepository.findByUuid(uuid))

}
