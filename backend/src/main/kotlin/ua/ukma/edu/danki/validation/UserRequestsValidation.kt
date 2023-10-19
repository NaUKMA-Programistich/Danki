package ua.ukma.edu.danki.validation

import io.ktor.server.plugins.requestvalidation.*
import ua.ukma.edu.danki.models.*
import ua.ukma.edu.danki.utils.consts.INCORRECT_CREDENTIALS_MESSAGE
import ua.ukma.edu.danki.utils.isEmailValid
import java.util.*

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

    validate<UpdateCollectionRequest> { body ->
        if (body.name.isNullOrBlank())
            ValidationResult.Invalid("Name cannot be blank")
        else {
            try {
                UUID.fromString(body.uuid)
                ValidationResult.Valid
            } catch (e: IllegalArgumentException) {
                ValidationResult.Invalid("Invalid UUID")
            }
        }
    }

    validate<CreateCardCollectionRequest> { body ->
        if (body.name.isBlank())
            ValidationResult.Invalid("Name cannot be blank")
        ValidationResult.Valid
    }

    validate<ShareCollectionRequest> { body ->
        try {
            UUID.fromString(body.uuid)
            ValidationResult.Valid
        } catch (e: IllegalArgumentException) {
            ValidationResult.Invalid("Invalid UUID")
        }
    }

    validate<DeleteCollectionsRequest> { body ->
        try {
            body.collections.forEach {
                UUID.fromString(it)
            }
            ValidationResult.Valid
        } catch (e: IllegalArgumentException) {
            ValidationResult.Invalid("One or more invalid UUIDs")
        }
    }
}