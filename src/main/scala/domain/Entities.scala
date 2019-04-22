package domain

import java.time.LocalDate


case class User(uuid: Option[String], name: String, email: String, birthDate: LocalDate)