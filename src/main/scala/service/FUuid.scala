package service

import io.chrisdavenport.fuuid.FUUID
import scalaz.zio.interop.catz._
import scalaz.zio.{Task, TaskR}

trait UUID {

  def uuidGen: UUID.Service

}

object UUID {

  trait Service {

    def genUUID(): TaskR[Any, String]

  }

}

object UUIDGen extends UUID.Service {

  type UUIDTask[A] = TaskR[Any, A]

  override def genUUID(): TaskR[Any, String] = {
    FUUID.randomFUUID[Task].map(_.toString())
  }

}
