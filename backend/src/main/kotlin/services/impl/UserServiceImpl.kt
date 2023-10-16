package services.impl

import exceptions.EmailTakenException
import models.User
import models.Users
import services.UserService
import ua.ukma.edu.danki.models.UserAuthRequest
import ua.ukma.edu.danki.models.UserRegisterRequest
import utils.DatabaseFactory
import utils.JwtConfig
import utils.PasswordEncoder
import utils.impl.PasswordEncoderImpl

class UserServiceImpl : UserService {
    private val passwordEncoder: PasswordEncoder = PasswordEncoderImpl

    override suspend fun authenticateUser(userAuthRequest: UserAuthRequest): String? {
        val existingUser =
            getExistingUser(userAuthRequest.email) ?: return null
        return if (passwordEncoder.isSame(userAuthRequest.password, existingUser.password))
            JwtConfig.makeToken(userAuthRequest)
        else
            null
    }

    override suspend fun registerUser(userRegisterRequest: UserRegisterRequest) {
        val existingUser = getExistingUser(userRegisterRequest.email)
        if (existingUser != null)
            throw EmailTakenException("Email has already been taken")
        createNewUser(userRegisterRequest)
    }

    private suspend fun createNewUser(userRegisterRequest: UserRegisterRequest) {
        DatabaseFactory.dbQuery {
            User.new {
                username = userRegisterRequest.username
                email = userRegisterRequest.email
                password = passwordEncoder.encode(userRegisterRequest.password)
            }
        }
    }

    private suspend fun getExistingUser(email: String): User? {
        return DatabaseFactory.dbQuery {
            User.find { Users.email eq email }.singleOrNull()
        }
    }
}