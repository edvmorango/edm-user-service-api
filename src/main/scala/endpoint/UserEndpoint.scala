package endpoint

import endpoint.json.JsonSupport
import endpoint.request.CreateUserRequest
import io.circe.generic.auto._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.impl.QueryParamDecoderMatcher
import repository.UserRepository
import scalaz.zio.TaskR
import scalaz.zio.interop.catz._
import service.UUID

final class UserEndpoint[R <: UserRepository with UUID](rootUri: String)
    extends JsonSupport[R] {

  import request.UserRequestBridge._
  import service.UserServiceImpl._

  object EmailQueryParamMatcher
      extends QueryParamDecoderMatcher[String]("email")

  type UserTask[A] = TaskR[R, A]

  val dsl: Http4sDsl[UserTask] = Http4sDsl[UserTask]

  import dsl._

  def endpoints: HttpRoutes[UserTask] =
    HttpRoutes.of[UserTask] {

      case GET -> Root / `rootUri` :? EmailQueryParamMatcher(email) =>
        findByEmail(email).flatMap {
          case None       => NotFound()
          case Some(user) => Ok(user)
        }

      case req @ POST -> Root / `rootUri` =>
        val res = req
          .as[CreateUserRequest]
          .map(_.toDomain()) >>= createUser

        Created(res)
    }

}
