package ru.itis.androidhomework.exeption

sealed class AppHttpException(message: String) : Throwable(message) {

    data class NetworkError(override val message: String) : AppHttpException(message)

    data class BadRequest(override val message: String) : AppHttpException(message)

    data class Unauthorized(override val message: String) : AppHttpException(message)

    data class Forbidden(override val message: String) : AppHttpException(message)

    data class NotFound(override val message: String) : AppHttpException(message)

    data class TooManyRequests(override val message: String) : AppHttpException(message)

    data class HttpError(val code: Int, override val message: String) : AppHttpException(message)

    data class ValidationError(override val message: String) : AppHttpException(message)

    data class WrongCoordinatesError(override val message: String) : AppHttpException(message)

    data class WrongDetailError(override val message: String) : AppHttpException(message)

    data class UnknownError(override val message: String) : AppHttpException(message)
}

