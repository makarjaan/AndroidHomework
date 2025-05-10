package ru.itis.androidhomework.domain.exeption

sealed class AppHttpException(message: String) : Throwable(message) {

    class BadRequest(message: String) : AppHttpException(message)

    class Unauthorized(message: String) : AppHttpException(message)

    class Forbidden(message: String) : AppHttpException(message)

    class TooManyRequests(message: String) : AppHttpException(message)

    class NotFound(message: String) : AppHttpException(message)

    class HttpError(val code: Int, message: String) : AppHttpException(message)

    class NetworkError(message: String) : AppHttpException(message)

    class ValidationError(message: String) : AppHttpException(message)

    class UnknownError(message: String) : AppHttpException(message)
}

