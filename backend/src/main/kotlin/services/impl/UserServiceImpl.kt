package services.impl

import exceptions.EmailTakenException
import models.User
import models.Users
import org.jetbrains.exposed.sql.transactions.transaction
import services.UserService
import ua.ukma.edu.danki.models.UserAuthRequest
import ua.ukma.edu.danki.models.UserRegisterRequest
import utils.JwtConfig
import utils.PasswordEncoder
import utils.impl.PasswordEncoderImpl

class UserServiceImpl : UserService {
    private val passwordEncoder: PasswordEncoder = PasswordEncoderImpl

    override suspend fun authenticateUser(userAuthRequest: UserAuthRequest): String? {
        val existingUser =
            transaction { User.find { Users.email eq userAuthRequest.email }.singleOrNull() } ?: return null
        return if (passwordEncoder.isSame(userAuthRequest.password, existingUser.password))
            JwtConfig.makeToken(userAuthRequest)
        else
            null
    }

    override suspend fun registerUser(userRegisterRequest: UserRegisterRequest) {
        val existingUser = transaction { User.find { Users.email eq userRegisterRequest.email }.singleOrNull() }
        if (existingUser != null)
            throw EmailTakenException("Email has already been taken")

        transaction {
            User.new {
                username = userRegisterRequest.username
                email = userRegisterRequest.email
                password = passwordEncoder.encode(userRegisterRequest.password)
            }
        }
    }
}