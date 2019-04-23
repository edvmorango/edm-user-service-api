package interop

import io.chrisdavenport.fuuid.FUUID
import scalaz.zio.Task
import scalaz.zio.interop.catz._

object FUuid {

  def getUUID[E](): Task[String] = {

    FUUID.randomFUUID
      .map(_.toString)

  }

}
