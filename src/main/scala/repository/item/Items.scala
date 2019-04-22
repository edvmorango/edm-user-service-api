package repository.item

import java.time.LocalDate

import domain.User


case class UserItem(uuid: String, name: String, email: String, birthDate: String)



object UserBridge {

  implicit class UserItemOps(ui: UserItem) {

    def toDomain(): User = User(Some(ui.uuid), ui.name, ui.email,  LocalDate.parse(ui.birthDate) )

  }

  implicit class UserOps(u: User)  {

    def toItem(): UserItem = UserItem(u.uuid.get, u.name, u.email, u.birthDate.toString )

  }


}
