package repository

import domain.User
import scalaz.zio.{Ref, ZIO}

final class UserRepositoryInMemory(ref: Ref[Map[String, User]])
    extends UserRepository.Repository {

  override def findByUuid(uuid: String): ZIO[Any, Throwable, Option[User]] =
    ref.get.map(_.get(uuid))

  override def findByEmail(email: String): ZIO[Any, Throwable, Option[User]] =
    ref.get.map(els => els.find(_._2.email == email).map(_._2))

  override def createUser(user: User): ZIO[Any, Throwable, Unit] =
    for {
      _ <- ref.update(els => els + (user.uuid.get -> user))
    } yield ()
}
