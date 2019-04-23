package repository

import cats.free.Free
import com.gu.scanamo.error.DynamoReadError
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

    def findByEmail(email: String): ZIO[R, Throwable, Option[User]]

    def createUser(user: User): ZIO[R, Throwable, Unit]

  }

}
final class UserRepositoryDynamoDB(db: DynamoDB.Client)
    extends Repository[Any] {

  import com.gu.scanamo._
  import com.gu.scanamo.ops.ScanamoOpsA
  import com.gu.scanamo.syntax._
  import repository.item.UserBridge._

  private val table = Table[UserItem]("user")
  private val idxEmail = table.index("idx_email")

  private def run[A](op: Free[ScanamoOpsA, A]): ZIO[Any, Throwable, A] =
    ZIO.fromFuture(implicit ec => ScanamoAlpakka.exec(db.instance)(op))

  def findByUuid(uuid: String): ZIO[Any, Throwable, Option[User]] = {

    val op = for {
      r <- table.get('uuid -> uuid)
    } yield r.flatMap(_.toOption)

    run(op).map(u => u.map(_.toDomain()))

  }

  def findByEmail(email: String): ZIO[Any, Throwable, Option[User]] = {

    val op = for {
      r <- idxEmail.query('email -> email)
    } yield r.flatMap(_.toOption).headOption

    run(op).map(u => u.map(_.toDomain()))

  }

  def createUser(user: User): ZIO[Any, Throwable, Unit] = {

    val op = for {
      _ <- table.put(user.toItem())
      res <- table.get('id -> user.uuid.get)
    } yield res.get

    for {
      opr <- run(op)
      res <- opr match {
        case Right(_)                 => ZIO.unit
        case Left(e: DynamoReadError) => ZIO.fail(new Exception(e.toString))
      }

    } yield res

  }

}
