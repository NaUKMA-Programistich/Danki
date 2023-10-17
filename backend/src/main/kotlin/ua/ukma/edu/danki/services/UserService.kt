package ua.ukma.edu.danki.services

import ua.ukma.edu.danki.models.User
import ua.ukma.edu.danki.models.UserAuthRequest
import ua.ukma.edu.danki.models.UserRegisterRequest
import java.util.UUID

interface UserService {
    /**
     * @param userAuthRequest - object with info of the user to be authenticated
     * @return JWT for the user, or null if unsuccessful
     */
    suspend fun authenticateUser(userAuthRequest: UserAuthRequest): String?

    /**
     * @param userRegisterRequest - object with info of the user to be registered
     * @throws UserRegistrationException
     */
    suspend fun registerUser(userRegisterRequest: UserRegisterRequest)

    /**
     * @param uuid - id of the user to find
     * @return user, null if not found
     */
    suspend fun findUser(uuid: UUID): User?
}