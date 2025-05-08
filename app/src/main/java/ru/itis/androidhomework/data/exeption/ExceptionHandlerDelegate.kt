package ru.itis.androidhomework.data.exeption

import retrofit2.HttpException
import ru.itis.androidhomework.R
import ru.itis.androidhomework.domain.exeption.AppHttpException
import ru.itis.androidhomework.domain.exeption.WrongCoordinatesException
import ru.itis.androidhomework.utils.ResManager
import java.io.IOException
import javax.inject.Inject

class ExceptionHandlerDelegate @Inject constructor(
    private val resManager: ResManager,
) {
    fun handleException(ex: Throwable): AppHttpException {

        return when (ex) {
            is HttpException -> {

                when (ex.code()) {
                    400 -> AppHttpException.BadRequest(
                        resManager.getString(R.string.error_bad_request)
                    )

                    401 -> AppHttpException.Unauthorized(
                        resManager.getString(R.string.error_unauthorized)
                    )
                    403 -> AppHttpException.Forbidden(
                        resManager.getString(R.string.error_forbidden)
                    )
                    404 -> AppHttpException.NotFound(
                        resManager.getString(R.string.error_not_found)
                    )
                    429 -> AppHttpException.TooManyRequests(
                        resManager.getString(R.string.error_too_many_requests)
                    )
                    else -> AppHttpException.HttpError(
                        ex.code(),
                        resManager.getString(R.string.error_http_generic)
                    )
                }
            }

            is IOException -> AppHttpException.NetworkError(
                resManager.getString(R.string.error_network)
            )

            is IllegalArgumentException -> AppHttpException.ValidationError(
                resManager.getString(R.string.error_validation)
            )

            is WrongCoordinatesException -> AppHttpException.NotFound(
                resManager.getString(R.string.error_coordinates_not_found)
            )

            else -> AppHttpException.UnknownError(
                resManager.getString(R.string.error_unknown)
            )
        }
    }
}
