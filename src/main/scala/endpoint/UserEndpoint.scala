package endpoint

import endpoint.json.JsonSupport
import endpoint.response.HealthResponse
import io.circe.generic.auto._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import scalaz.zio.TaskR
import scalaz.zio.interop.catz._
import service.User

final class UserEndpoint[R <: User](rootUri: String) extends JsonSupport[R] {

  type UserTask[A] = TaskR[R, A]

  val dsl: Http4sDsl[UserTask] = Http4sDsl[UserTask]

  import dsl._

  def endpoints: HttpRoutes[UserTask] = HttpRoutes.of[UserTask] {

    case GET -> Root / `rootUri` => Ok(HealthResponse())

  }

}
