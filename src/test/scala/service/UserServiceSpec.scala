package service

import java.time.LocalDate

import domain.User
import org.scalatest.{MustMatchers, WordSpec}
import repository.{UserRepository, UserRepositoryInMemory}
import scalaz.zio.{DefaultRuntime, Ref}

class UserServiceSpec extends WordSpec with MustMatchers with DefaultRuntime {

  val ref: Ref[Map[String, User]] = unsafeRun(Ref.make(Map.empty[String, User]))

  type SpecEnvironment = UserRepository with UUID

  val specEnvironment: SpecEnvironment = new UserRepository with UUID {
    override val userRepository: UserRepository.Repository =
      new UserRepositoryInMemory(ref)

    override def uuidGen: UUID.Service = UUIDGen
  }

  "UserService" should {

    val user = User(None,
                    "Eduardo Morango",
                    "jevmor@gmail.com",
                    LocalDate.of(1996, 6, 10))

    "Create a user" in {

      val iUser = unsafeRun {
        UserServiceImpl.createUser(user).provide(specEnvironment)
      }

      iUser.uuid mustNot be(None)
      iUser.name mustBe user.name
      iUser.email mustBe user.email
      iUser.birthDate mustBe user.birthDate

    }

    "Fail when creating a duplicate user" in {

      unsafeRun {
        UserServiceImpl
          .createUser(user)
          .either
          .provide(specEnvironment)
      }.isLeft mustBe true

    }

    "Find a user by email" in {

      unsafeRun {
        UserServiceImpl.findByEmail(user.email).provide(specEnvironment)
      } mustNot be(None)

    }

    "Not find a user by email" in {

      unsafeRun {
        UserServiceImpl
          .findByEmail("inexistentmail@mail.com")
          .provide(specEnvironment)
      } mustBe None

    }

  }

}
