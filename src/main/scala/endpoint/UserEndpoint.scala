package endpoint

import domain.User
import endpoint.json.JsonSupport
import endpoint.request.CreateUserRequest
import io.circe.generic.auto._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import repository.UserRepository
import scalaz.zio.{TaskR, ZIO}
import scalaz.zio.interop.catz._
import service.{UUID, UserModule, UserServiceImpl}

final class UserEndpoint[R <: UserModule](rootUri: String)
    extends JsonSupport[R] {

  import request.UserRequestBridge._

  type UserTask[A] = TaskR[R, A]

  val dsl: Http4sDsl[UserTask] = Http4sDsl[UserTask]

  import dsl._

  def endpoints: HttpRoutes[UserTask] = HttpRoutes.of[UserTask] {

    case GET -> Root / `rootUri` / id => Ok(id)

    case req @ POST -> Root / `rootUri` =>
      val user: ZIO[R, Throwable, User] =
        req.as[CreateUserRequest].map(_.toDomain())

      ZIO.accessM[UserModule] { env =>
        val xz: ZIO[R, Throwable, User] = req
          .as[CreateUserRequest]
          .map(_.toDomain())
          .flatMap(env.user.createUser)

        ???
      }

      ???
  }

}
