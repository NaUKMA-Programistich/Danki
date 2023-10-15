package services

import models.User
import ua.ukma.edu.danki.models.UserAuthRequest
import ua.ukma.edu.danki.models.UserRegisterRequest

interface UserService {
    /**
     * @param userAuthRequest - object with info of the user to be authenticated
     * @return JWT for the user, or null if unsuccessful
     */
    suspend fun authenticateUser(userAuthRequest: UserAuthRequest): String?

    /**
     * @param userRegisterRequest - object with info of the user to be registered
     * @return whether user is successfully registered
     */
    suspend fun registerUser(userRegisterRequest: UserRegisterRequest): User?
}