package exceptions

import utils.consts.ILLEGAL_ACCESS_ATTEMPT_MESSAGE

open class BadRequestException(msg: String) : Exception(msg)

class ResourceNotFoundException(msg: String) : BadRequestException(msg)

open class UserRegistrationException(msg: String) : BadRequestException(msg)

class EmailTakenException(msg: String) : UserRegistrationException(msg)

class IllegalAccessException(msg: String = ILLEGAL_ACCESS_ATTEMPT_MESSAGE) : BadRequestException(msg)