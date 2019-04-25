package endpoint

import domain.User
import endpoint.json.JsonSupport
import endpoint.request.CreateUserRequest
import io.circe.generic.auto._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import repository.UserRepository
import scalaz.zio.interop.catz._
import scalaz.zio.{TaskR, ZIO}
import service.UUID

final class UserEndpoint[R <: UserRepository with UUID](rootUri: String)
    extends JsonSupport[R] {

  import request.UserRequestBridge._
  import service.UserServiceImpl._

  type UserTask[A] = TaskR[R, A]

  val dsl: Http4sDsl[UserTask] = Http4sDsl[UserTask]

  import dsl._

  def endpoints: HttpRoutes[UserTask] =
    HttpRoutes.of[UserTask] {

      case GET -> Root / `rootUri` / id => Ok(id)

      case req @ POST -> Root / `rootUri` =>
        val user: ZIO[R, Throwable, User] =
          req.as[CreateUserRequest].map(_.toDomain())

        Created apply user.flatMap(createUser)

    }

}
