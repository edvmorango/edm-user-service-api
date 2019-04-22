package repository

import domain.User
import scalaz.zio.ZIO

trait UserRepository[R] {

  def findByUuid(uuid: String): ZIO[R, Throwable, Option[User]]

  def createUser(user: User): ZIO[R, Throwable, Unit]

}

class UserRepositoryDynamoDB[R] extends UserRepository[R] {

  def findByUuid(uuid: String): ZIO[R, Throwable, Option[User]] = ???

  def createUser(user: User): ZIO[R, Throwable, Unit] = ???

}
