package endpoint.request

import java.time.LocalDate

import domain.User

case class CreateUserRequest(name: String, email: String, birthDate: LocalDate)

object UserRequestBridge {

  implicit class CreateUserRequestOps(cur: CreateUserRequest) {

    def toDomain(): User =
      User(None, cur.name, cur.email, cur.birthDate)

  }

}
