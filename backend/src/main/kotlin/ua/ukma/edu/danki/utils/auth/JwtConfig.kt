package ua.ukma.edu.danki.utils.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.auth.jwt.*
import ua.ukma.edu.danki.models.auth.UserAuthRequest
import ua.ukma.edu.danki.services.UserService
import ua.ukma.edu.danki.services.impl.UserServiceImpl
import java.util.*

object JwtConfig {
    private var secret = "secret"
    private var issuer = "danki"
    private var validityInMs = 36_000_00L * 10 // 10 hours
    private var _verifier: JWTVerifier? = null
    private var userService: UserService = UserServiceImpl()

    fun init(secret: String, issuer: String, validityInMs: Long, userService: UserService) {
        _verifier = null
        JwtConfig.secret = secret
        JwtConfig.issuer = issuer
        JwtConfig.validityInMs = validityInMs
        JwtConfig.userService = userService
    }

    fun makeToken(user: UserAuthRequest): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withClaim("email", user.email)
        .withExpiresAt(getExpiration())
        .sign(Algorithm.HMAC512(secret))

    private fun getExpiration() = Date(System.currentTimeMillis() + validityInMs)

    val verifier: JWTVerifier
        get() {
            if (_verifier == null) {
                _verifier = JWT
                    .require(Algorithm.HMAC512(secret))
                    .withIssuer(issuer)
                    .build()
            }
            return _verifier!!
        }

    suspend fun validate(credential: JWTCredential): JWTPrincipal? {
        val email = credential.payload.getClaim("email").asString()
        return if (email.isNotEmpty() &&
            userService.findUser(email) != null &&
            credential.payload.expiresAt.after(Date())
        ) {
            JWTPrincipal(credential.payload)
        } else {
            null
        }
    }
}