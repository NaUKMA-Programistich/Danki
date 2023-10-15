package dal.user

import models.User
import models.Users
import org.jetbrains.exposed.sql.transactions.transaction
import ua.ukma.edu.danki.models.UserRegisterRequest
import utils.PasswordEncoder
import utils.impl.PasswordEncoderImpl

private val passwordEncoder: PasswordEncoder = PasswordEncoderImpl

fun findUserByEmail(email: String): User? {
    return transaction { User.find { Users.email eq email }.singleOrNull() }
}

fun insertUser(userRegisterRequest: UserRegisterRequest): User {
    return transaction {
        User.new {
            username = userRegisterRequest.username
            email = userRegisterRequest.email
            password = passwordEncoder.encode(userRegisterRequest.password)
        }
    }
}