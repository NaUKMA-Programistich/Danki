package utils

import com.auth0.jwt.*
import com.auth0.jwt.algorithms.*
import io.ktor.server.auth.jwt.*
import models.User
import models.Users
import org.jetbrains.exposed.sql.transactions.transaction
import ua.ukma.edu.danki.models.UserAuthRequest
import java.util.*

object JwtConfig {
    private var secret = "secret"
    private var issuer = "danki"
    private var validityInMs = 36_000_00L * 10 // 10 hours
    private var _verifier: JWTVerifier? = null

    fun init(secret: String, issuer: String, validityInMs: Long) {
        _verifier = null
        this.secret = secret
        this.issuer = issuer
        this.validityInMs = validityInMs
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

    fun validate(credential: JWTCredential): JWTPrincipal? {
        val email = credential.payload.getClaim("email").asString()
        return if (email.isNotEmpty() &&
            transaction { User.find { Users.email eq email }.singleOrNull() } != null &&
            credential.payload.expiresAt.after(Date())
        ) {
            JWTPrincipal(credential.payload)
        } else {
            null
        }
    }
}