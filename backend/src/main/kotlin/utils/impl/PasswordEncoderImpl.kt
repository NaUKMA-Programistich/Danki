package utils.impl

import org.springframework.security.crypto.bcrypt.BCrypt
import utils.PasswordEncoder

object PasswordEncoderImpl : PasswordEncoder {
    override fun encode(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    override fun isSame(plaintextPassword: String, hashedPassword: String): Boolean {
        return BCrypt.checkpw(plaintextPassword, hashedPassword)
    }
}