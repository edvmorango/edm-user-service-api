package repository

import cats.free.Free
import domain.User
import repository.UserRepository.Repository
import repository.item.UserItem
import scalaz.zio.ZIO

trait UserRepository {

  val userRepository: UserRepository.Repository[Any]

}

object UserRepository {

  trait Repository[R] {

    def findByUuid(uuid: String): ZIO[R, Throwable, Option[User]]

    def createUser(user: User): ZIO[R, Throwable, Unit]

  }

}
final class UserRepositoryDynamoDB(db: DynamoDB.Client)
    extends Repository[Any] {

  import com.gu.scanamo._
  import com.gu.scanamo.ops.ScanamoOpsA
  import com.gu.scanamo.syntax._
  import repository.item.UserBridge.UserItemOps

  private val table = Table[UserItem]("user")

  private def run[A](op: Free[ScanamoOpsA, A]): ZIO[Any, Throwable, A] =
    ZIO.fromFuture(implicit ec => ScanamoAlpakka.exec(db.instance)(op))

  def findByUuid(uuid: String): ZIO[Any, Throwable, Option[User]] = {

    val op = for {
      r <- table.get('uuid -> uuid)
    } yield r.flatMap(_.toOption)

    run(op).map(u => u.map(_.toDomain()))

  }

  def createUser(user: User): ZIO[Any, Throwable, Unit] = ???

}
