package services.impl

import dal.user.findUserByEmail
import dal.user.insertUser
import models.User
import services.UserService
import ua.ukma.edu.danki.models.UserAuthRequest
import ua.ukma.edu.danki.models.UserRegisterRequest
import utils.JwtConfig
import utils.PasswordEncoder
import utils.impl.PasswordEncoderImpl

class UserServiceImpl : UserService {
    private val passwordEncoder: PasswordEncoder = PasswordEncoderImpl

    override suspend fun authenticateUser(userAuthRequest: UserAuthRequest): String? {
        val existingUser = findUserByEmail(userAuthRequest.email) ?: return null
        return if (passwordEncoder.isSame(userAuthRequest.password, existingUser.password))
            JwtConfig.makeToken(userAuthRequest)
        else
            null
    }

    override suspend fun registerUser(userRegisterRequest: UserRegisterRequest): User? {
        findUserByEmail(userRegisterRequest.email) ?: return null
        return insertUser(userRegisterRequest)
    }
}