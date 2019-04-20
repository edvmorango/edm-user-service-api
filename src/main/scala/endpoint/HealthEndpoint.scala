package endpoint

import endpoint.json.JsonSupport
import endpoint.response.HealthResponse
import io.circe.generic.auto._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import scalaz.zio.TaskR
import scalaz.zio.interop.catz._

class HealthEndpoint extends JsonSupport {

  type HealthTask[A] = TaskR[Any, A]

  val dsl: Http4sDsl[HealthTask] = Http4sDsl[HealthTask]

  import dsl._

  def endpoints: HttpRoutes[HealthTask] = HttpRoutes.of[HealthTask] {

    case GET -> Root / "health" => Ok(new HealthResponse())

  }

}
