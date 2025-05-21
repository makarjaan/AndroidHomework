package ru.itis.androidhomework.common.exeption

sealed class AppBusinessException(cause: Throwable?): Throwable(cause) {

    class WrongCoordinatesException(cause: Throwable?): AppBusinessException(cause)

    class WrongDetailsException(cause: Throwable?): AppBusinessException(cause)
}
