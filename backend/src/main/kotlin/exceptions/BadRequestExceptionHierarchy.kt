package exceptions

open class BadRequestException(msg: String) : Exception(msg)

open class UserRegistrationException(msg: String) : BadRequestException(msg)

class EmailTakenException(msg: String) : UserRegistrationException(msg)