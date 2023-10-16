package services.impl

import exceptions.EmailTakenException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
        withContext(Dispatchers.IO) {
            transaction {
                User.new {
                    username = userRegisterRequest.username
                    email = userRegisterRequest.email
                    password = passwordEncoder.encode(userRegisterRequest.password)
                }
            }
        }
    }

    private suspend fun getExistingUser(email: String): User? {
        return withContext(Dispatchers.IO) {
            transaction {
                User.find { Users.email eq email }.singleOrNull()
            }
        }
    }
}