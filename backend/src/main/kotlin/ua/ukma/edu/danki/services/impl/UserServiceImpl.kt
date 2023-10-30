package ua.ukma.edu.danki.services.impl

import org.jetbrains.exposed.dao.id.EntityID
import ua.ukma.edu.danki.exceptions.EmailTakenException
import ua.ukma.edu.danki.models.*
import ua.ukma.edu.danki.services.UserService
import ua.ukma.edu.danki.utils.DatabaseFactory
import ua.ukma.edu.danki.utils.auth.JwtConfig
import ua.ukma.edu.danki.utils.PasswordEncoder
import ua.ukma.edu.danki.utils.impl.PasswordEncoderImpl
import java.util.*

class UserServiceImpl : UserService {
    private val passwordEncoder: PasswordEncoder = PasswordEncoderImpl

    override suspend fun authenticateUser(userAuthRequest: UserAuthRequest): String? {
        val existingUser =
            getExistingUserByEmail(userAuthRequest.email) ?: return null
        return if (passwordEncoder.isSame(userAuthRequest.password, existingUser.password))
            JwtConfig.makeToken(userAuthRequest)
        else
            null
    }

    override suspend fun registerUser(userRegisterRequest: UserRegisterRequest) {
        val existingUser = getExistingUserByEmail(userRegisterRequest.email)
        if (existingUser != null)
            throw EmailTakenException("Email has already been taken")
        createNewUser(userRegisterRequest)
    }

    override suspend fun findUser(uuid: UUID): User? {
        return getExistingUserById(uuid)
    }

    override suspend fun findUser(email: String): User? {
        return getExistingUserByEmail(email)
    }

    private suspend fun createNewUser(userRegisterRequest: UserRegisterRequest) {
        DatabaseFactory.dbQuery {
            val createdUser = User.new {
                username = userRegisterRequest.username
                email = userRegisterRequest.email
                password = passwordEncoder.encode(userRegisterRequest.password)
            }
            createRecentCardCollection(createdUser.id)
        }
    }

    private fun createRecentCardCollection(userId: EntityID<UUID>) {
        val cardCollection = CardCollection.new {
           name = "Recent"
        }

        UserCardCollection.new {
            user = userId
            hidden = true
            type = CollectionType.Recents
            collection = cardCollection.id
        }
    }

    private suspend fun getExistingUserByEmail(email: String): User? {
        return DatabaseFactory.dbQuery {
            User.find { Users.email eq email }.singleOrNull()
        }
    }

    private suspend fun getExistingUserById(uuid: UUID): User? {
        return DatabaseFactory.dbQuery {
            User.find { Users.id eq uuid }.singleOrNull()
        }
    }
}