package ua.ukma.edu.danki.validation

import io.ktor.server.plugins.requestvalidation.*
import ua.ukma.edu.danki.models.UserAuthRequest
import ua.ukma.edu.danki.models.UserRegisterRequest
import ua.ukma.edu.danki.utils.consts.INCORRECT_CREDENTIALS_MESSAGE
import ua.ukma.edu.danki.utils.isEmailValid

fun RequestValidationConfig.validateUserRequests() {
    validate<UserAuthRequest> { body ->
        if (body.password.isEmpty() || body.email.isEmpty() || !isEmailValid(body.email))
            ValidationResult.Invalid(INCORRECT_CREDENTIALS_MESSAGE)
        else ValidationResult.Valid
    }

    validate<UserRegisterRequest> { body ->
        if (body.email.isEmpty() || !isEmailValid(body.email))
            ValidationResult.Invalid("Provided email is invalid")
        else if (body.password.isEmpty())
            ValidationResult.Invalid("Password cannot be empty")
        else if (body.username.isEmpty())
            ValidationResult.Invalid("Username cannot be empty")
        else ValidationResult.Valid
    }
}